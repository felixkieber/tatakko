package li.flxkbr
package schema.generator

import schema.generator.*
import schema.codec.*

import io.circe.Decoder.Result
import io.circe.{Decoder, HCursor}

trait GeneratorType(val value: String) {
  def columnDecoder: Decoder[Column]
}

object GeneratorType {

  import ParameterDecoders.given

  case object Serial extends GeneratorType("serial") {
    override lazy val columnDecoder: Decoder[Column] = ColumnDecoders.noParametersColumnDecoder(Serial)
  }

  case object Int extends GeneratorType("int") {
    override val columnDecoder: Decoder[Column] = ColumnDecoders.intColumnDecoder
  }

  case object String extends GeneratorType("string") {
    override val columnDecoder: Decoder[Column] = ColumnDecoders.stringColumnDecoder
  }

  case object Instant extends GeneratorType("instant") {
    override val columnDecoder: Decoder[Column] = ColumnDecoders.instantColumnDecoder
  }

  case object Reference extends GeneratorType("ref") {
    override def columnDecoder: Decoder[Column] = ColumnDecoders.referenceColumnDecoder
  }

  val values: Seq[GeneratorType] = List(
    Serial,
    Int,
    String,
    Instant,
    Reference,
  )

  def ofValue(value: String): Option[GeneratorType] = {
    values.find(_.value == value)
  }
}
