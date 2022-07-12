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
    override def columnDecoder: Decoder[Column] = ColumnDecoders.serialColumnDecoder
  }

  case object Int extends GeneratorType("int") {
    override def columnDecoder: Decoder[Column] = ???
  }

  case object String extends GeneratorType("string") {
    override def columnDecoder: Decoder[Column] = ???
  }

  case object Instant extends GeneratorType("instant") {
    override def columnDecoder: Decoder[Column] = ???
  }

  val values: Seq[GeneratorType] = List(
    Serial,
    Int,
    String,
    Instant,
  )

  def ofValue(value: String): Option[GeneratorType] = {
    values.find(_.value == value)
  }
}
