package li.flxkbr.gen

import li.flxkbr.testutil.*
import li.flxkbr.schema.generator.*
import org.scalatest.LoneElement.*
import scala.util.Try
import org.scalatest.matchers.Matcher
import org.scalatest.matchers.MatchResult

class PlanSpec extends UnitSpec {

  "Plan" should "build a single stage plan from dependency-free schemas" in {

    val schemas = (for i <- 1 to 5 yield rootSchema(i.toString)).toSet

    type EitherThrow[A] = Either[Throwable, A]
    val build = Plan.build[EitherThrow](schemas)

    build.right.value shouldBe Plan(Seq(schemas.map(Generate(_, false))))
  }

  it should "correctly build a plan with dependencies" in {

    // stage 0
    val root0 = rootSchema("alpha")
    val root1 = rootSchema("beta")

    // stage 1
    val ref0Stage1 = referenceSchema("eins", ColumnPath(root0.name, root0.columns(0).name))
    val ref1Stage1 = referenceSchema("zwei", ColumnPath(root1.name, root1.columns(1).name))

    // stage 2
    val ref0Stage2 = referenceSchema(
      "as",
      ColumnPath(root0.name, root0.columns(0).name),
      ColumnPath(ref0Stage1.name, ref0Stage1.columns(0).name),
    )

    // stage 3
    val ref0Stage3 = referenceSchema("yeb", ColumnPath(ref0Stage2.name, ref0Stage2.columns(0).name))

    val allSchemas = Set(root0, root1, ref0Stage1, ref1Stage1, ref0Stage2, ref0Stage3)

    val expectedPlan = Plan(
      Seq(
        Set(Generate(root0, cache = true), Generate(root1, cache = true)),
        Set(Generate(ref0Stage1, cache = true), Generate(ref1Stage1, cache = false)),
        Set(Drop(root1.name), Generate(ref0Stage2, cache = true)),
        Set(Drop(root0.name), Drop(ref0Stage1.name), Generate(ref0Stage3, cache = false)),
      )
    )

    Plan.build[Try](allSchemas).success.value shouldBe expectedPlan
  }

  it should "fail when empty set of schemas is passed" in {

    Plan.build[Try](Set.empty).failure.exception shouldBe an [IllegalArgumentException]
  }

  // UTILS

  def basicColumn(name: String): Column =
    Column(ColumnName.convert(name), GeneratorType.Serial, NoParameters, primaryKey = false)

  def referenceColumn(name: String, target: ColumnPath) =
    Column(ColumnName.convert(name), GeneratorType.Reference, ReferenceParameters(target), primaryKey = false)

  def rootSchema(name: String, columns: Int = 3) =
    Schema(SchemaName.convert(name), count = 20, (0 until columns).map(i => basicColumn(i.toString)))

  def referenceSchema(name: String, target: ColumnPath, targets: ColumnPath*) = {
    Schema(
      SchemaName.convert(name),
      count = 20,
      (target +: targets).zipWithIndex.map((cp, i) => referenceColumn(i.toString, cp)),
    )
  }
}
