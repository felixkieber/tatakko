package li.flxkbr.model

import cats.MonadError
import cats.MonadThrow
import cats.effect.instances.AllInstances
import cats.implicits.catsSyntaxTuple2Semigroupal

opaque type FieldName = String

private def safeName[F[_]](name: String)(using me: MonadThrow[F]): F[String] = {
  if name.isEmpty then me.raiseError(ModelPath.EmptyNameException)
    else me.pure(name)
}

object FieldName {
  def safe[F[_]](name: String)(using me: MonadThrow[F]): F[FieldName] = safeName(name)
}

opaque type ModelName = String

object ModelName {
  def safe[F[_]](name: String)(using me: MonadThrow[F]): F[ModelName] = safeName(name)
}

case class ModelPath(modelName: ModelName, fieldName: FieldName)

object ModelPath {

  object EmptyNameException                       extends Exception("Given element name is empty")
  case class MalformedPathException(path: String) extends Exception(s"Path $path is malformed")

  def fromString[F[_]](rawPath: String)(using me: MonadThrow[F]) = {
    val split = rawPath.split(".")
    if split.length != 2 || split(0).isEmpty || split(1).isEmpty then me.raiseError(MalformedPathException(rawPath))
    else (ModelName.safe(split(0)), FieldName.safe(split(1))).mapN(ModelPath.apply)
  }
}
