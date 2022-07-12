package li.flxkbr
package schema.generator

import io.circe.{Decoder, DecodingFailure}

import java.time.Instant
import scala.util.Try

trait ColumnParameters

object NoParameters                                                   extends ColumnParameters
type NoParameters = NoParameters.type
case class IntParameters(min: Int, max: Int)                          extends ColumnParameters
case class StringParameters(domain: Option[String], cardinality: Int) extends ColumnParameters
case class InstantParameters(start: Instant, end: Instant)            extends ColumnParameters

object ParameterDecoders {

  protected implicit val instantDecoder: Decoder[Instant] = Decoder.instance { c =>
    c.as[String].flatMap {
      case "now" => Right(Instant.now())
      case time  => Try(Instant.parse(time)).toEither.left.map(ex => DecodingFailure.fromThrowable(ex, c.history))
    }
  }

  given noParametersDecoder: Decoder[NoParameters] = Decoder.const(NoParameters)
  
  given intParametersDecoder: Decoder[IntParameters] = Decoder.instance(c =>
    for {
      min <- c.getOrElse("min")(0)
      max <- c.getOrElse("max")(scala.Int.MaxValue)
    } yield IntParameters(min, max)
  )

  given stringParametersDecoder: Decoder[StringParameters] = Decoder.instance { c =>
    for {
      domain      <- c.get[Option[String]]("domain")
      cardinality <- c.getOrElse("cardinality")(-1)
    } yield StringParameters(domain, cardinality)
  }

  given instantParameterDecoder: Decoder[InstantParameters] = Decoder.instance { c =>
    for {
      start <- c.get[Instant]("start")(using instantDecoder)
      end   <- c.get[Instant]("end")
    } yield InstantParameters(start, end)
  }
}
