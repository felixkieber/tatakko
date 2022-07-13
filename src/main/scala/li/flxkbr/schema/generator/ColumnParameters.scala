package li.flxkbr.schema.generator

import li.flxkbr.schema.codec.BasicTypeCodecSupport
import io.circe.{Decoder, DecodingFailure}

import java.time.Instant
import scala.util.Try

trait ColumnParameters

object NoParameters                                                   extends ColumnParameters
type NoParameters = NoParameters.type
case class IntParameters(min: Int, max: Int)                          extends ColumnParameters
case class StringParameters(domain: Option[String], cardinality: Int) extends ColumnParameters
case class InstantParameters(start: Instant, end: Instant)            extends ColumnParameters
