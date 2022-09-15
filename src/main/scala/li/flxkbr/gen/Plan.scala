package li.flxkbr.gen

import cats.ApplicativeThrow
import cats.implicits.toFunctorOps
import li.flxkbr.schema.generator.Column
import li.flxkbr.schema.generator.ColumnPath
import li.flxkbr.schema.generator.ReferenceParameters
import li.flxkbr.schema.generator.Schema
import li.flxkbr.schema.generator.SchemaName

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import scala.util.Failure
import scala.util.Success
import scala.util.Try

case class Plan(stages: Seq[Set[Operation]])

sealed trait Operation

case class Generate(schema: Schema, cache: Boolean) extends Operation
case class Drop(schema: SchemaName)                 extends Operation

object Plan {

  def unsafeBuild(schemas: Set[Schema]): Plan = {
    build[Try](schemas).get
  }

  def build[F[_]](schemas: Set[Schema])(using ae: ApplicativeThrow[F]): F[Plan] = {

    if schemas.isEmpty then return ae.raiseError(IllegalArgumentException("Cannot build plan from empty schema list"))

    val references = schemas.flatMap { schema =>
      schema.columns.collect { case Column(_, _, ReferenceParameters(ColumnPath(referencedSchema, _)), _) =>
        schema.name -> referencedSchema
      }
    }

    val schemaByName = schemas.map(schema => schema.name -> schema).toMap

    val dependencies = references.groupMap(_._1)(_._2).withDefaultValue(Set.empty)
    val children     = references.groupMap(_._2)(_._1).withDefaultValue(Set.empty)

    val referencingSchemaNames = references.map(_._1)
    val referencedSchemaNames  = references.map(_._2)

    val (referencingSchemas, rootSchemas) = schemas.partition(s => referencingSchemaNames.contains(s.name))

    if rootSchemas.isEmpty then return ae.raiseError(NoRootSchemaFailure)

    var doneSchemas      = rootSchemas.map(_.name)
    var remainingSchemas = schemas.map(_.name) -- doneSchemas

    var cachedSchemas  = Set.empty[SchemaName] // CURRENTLY cached schemas
    var droppedSchemas = Set.empty[SchemaName] // dropped schemas

    def schemasWithoutRemainingChildren = {
      schemaByName.keySet.filter(sn => !children(sn).exists(remainingSchemas.contains))
    }

    val stages: ListBuffer[Set[Operation]] = ListBuffer.empty

    // first stage

    val rootStage =
      rootSchemas.map[Operation](s => Generate(schemaByName(s.name), cache = referencedSchemaNames.contains(s.name)))

    stages += rootStage
    cachedSchemas = cachedSchemas ++ rootStage.collect(collectCached)

    // recursively builds remaining stages

    @tailrec def buildStageRec(): F[Unit] = {
      if (remainingSchemas.isEmpty) {
        ae.pure(())
      } else {

        val satisfiedSchemas = remainingSchemas.filter(sn => dependencies(sn).forall(doneSchemas.contains))

        if (satisfiedSchemas.isEmpty) {
          ae.raiseError(UnmeetableDependenciesFailure(remainingSchemas.toSeq))
        } else {
          val dropSchemas = cachedSchemas.intersect(schemasWithoutRemainingChildren)
          droppedSchemas = droppedSchemas ++ dropSchemas
          cachedSchemas = cachedSchemas -- dropSchemas

          var stage: Set[Operation] = dropSchemas.map(Drop.apply)

          val satisfiedGenerateOps =
            satisfiedSchemas.map(sn => Generate(schemaByName(sn), cache = referencedSchemaNames.contains(sn)))
          cachedSchemas = cachedSchemas ++ satisfiedGenerateOps.collect(collectCached)

          stage = stage ++ satisfiedGenerateOps
          stages += stage

          doneSchemas = doneSchemas ++ satisfiedSchemas
          remainingSchemas = remainingSchemas -- satisfiedSchemas

          buildStageRec()
        }
      }
    }

    buildStageRec().map(_ => Plan(stages.toSeq))
  }

  private val collectCached: PartialFunction[Operation, SchemaName] = { case Generate(schema, true) =>
    schema.name
  }
}

sealed abstract class PlanBuildingFailure(msg: String) extends Exception(msg)

case object NoRootSchemaFailure
    extends PlanBuildingFailure("No schema without dependencies defined; No valid starting point.")

case class UnmeetableDependenciesFailure(schemas: Seq[SchemaName])
    extends PlanBuildingFailure(
      s"Unable to build valid dependency graph, dependencies of ${schemas.size} cannot be met."
    )
