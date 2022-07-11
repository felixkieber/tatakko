package li.flxkbr
package schema.codec

import io.circe.Decoder.Result
import io.circe.{Decoder, DecodingFailure, HCursor}
import schema.*
import schema.generator.*

given columnDecoder: Decoder[Column[_]] = new Decoder[Column[_]] {
  override final def apply(c: HCursor): Result[Column[_]] = {
    for {
      name <- c.key.toRight(DecodingFailure("Cannot retrieve key of column object", c.history)).map(ColumnName.convert)
      `type` <- c.downField("type").as[String].flatMap { t =>
        GeneratorType.ofValue(t).toRight(DecodingFailure(s"Invalid generator type $t", c.history))
      }
      parameters <- `type`.parameterDecoder.apply(c)
    } yield Column(name, `type`, parameters)
  }
}

given schemaDecoder: Decoder[Schema] = new Decoder[Schema] {
  override final def apply(c: HCursor): Result[Schema] = {
    ???
  }
}
