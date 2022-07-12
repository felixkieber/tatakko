package li.flxkbr
package schema.codec

import io.circe.Decoder.Result
import io.circe.{Decoder, DecodingFailure, HCursor}
import schema.*
import schema.generator.*

given columnDecoder: Decoder[Column] = new Decoder[Column] {
  override final def apply(c: HCursor): Result[Column] = {
    for {
      genType <- c.downField("type").as[String].flatMap { t =>
        GeneratorType.ofValue(t).toRight(DecodingFailure(s"Invalid generator type $t", c.history))
      }
      column <- c.as[Column](genType.columnDecoder)
    } yield column
  }
}

given schemaDecoder: Decoder[Schema] = new Decoder[Schema] {
  override final def apply(c: HCursor): Result[Schema] = {
    ???
  }
}
