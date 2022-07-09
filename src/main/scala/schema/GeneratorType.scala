package schema

import io.circe.Decoder

trait GeneratorType(val value: String) {
  def parameterDecoder: Decoder[ColumnParameters[this.type]]
}

object GeneratorType {

  case object Serial extends GeneratorType("serial") {
    override def parameterDecoder: Decoder[ColumnParameters[Serial.type]] = ???
  }

  case object Int extends GeneratorType("int") {
    override def parameterDecoder: Decoder[ColumnParameters[Int.type]] = ???
  }

  val values: Seq[GeneratorType] = List(
    Serial,
    Int,
  )

  def ofValue(value: String): Option[GeneratorType] = {
    values.find(_.value == value)
  }
}
