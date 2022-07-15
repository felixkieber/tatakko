package li.flxkbr.schema.codec

import io.circe.Decoder
import li.flxkbr.schema.generator.*
import java.time.Instant

object ParameterDecoders {

  import BasicTypeCodecs.given

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
