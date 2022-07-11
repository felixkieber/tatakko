package schema.generator

import io.circe.Decoder.Result
import io.circe.{Decoder, HCursor}
import schema.generator.*

trait GeneratorType(val value: String) {
  type P <: ColumnParameters
  def parameterDecoder: Decoder[P]
}

object GeneratorType {

  import ParameterDecoders.given

  case object Serial extends GeneratorType("serial") {
    type P = NoParameters.type
    override val parameterDecoder: Decoder[P] = summon
  }

  case object Int extends GeneratorType("int") {
    override type P = IntParameters
    override def parameterDecoder: Decoder[P] = summon
  }

  case object String extends GeneratorType("string") {
    override type P = StringParameters

    override def parameterDecoder: Decoder[StringParameters] = summon
  }

  case object Instant extends GeneratorType("instant") {
    override type P = InstantParameters

    override def parameterDecoder: Decoder[InstantParameters] = summon
  }

  val values: Seq[GeneratorType] = List(
    Serial,
    Int,
  )

  def ofValue(value: String): Option[GeneratorType] = {
    values.find(_.value == value)
  }
}
