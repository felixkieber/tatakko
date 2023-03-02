package li.flxkbr.model

import cats.data.NonEmptySeq
import io.circe.Codec

sealed trait SimpleModelField extends ModelField

case class SimpleIntField(name: FieldName, tpe: SimpleIntField.SimpleIntGenerator) extends SimpleModelField

object SimpleIntField {
  sealed trait SimpleIntGenerator
  case class Serial(start: Int = 0)                        extends SimpleIntGenerator
  case class Random(min: Int = 0, max: Int = Int.MaxValue) extends SimpleIntGenerator
}

case class DirectReferenceField(name: FieldName, path: ModelPath) extends SimpleModelField

case class SimpleStringField(name: FieldName, tpe: SimpleStringField.SimpleStringGenerator) extends SimpleModelField

object SimpleStringField {
  sealed trait SimpleStringGenerator
  case object UUIDString                          extends SimpleStringGenerator
  case class Select(options: NonEmptySeq[String]) extends SimpleStringGenerator
}
