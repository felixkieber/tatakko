package li.flxkbr.schema.codec

import io.circe.Decoder.Result
import io.circe.{Decoder, DecodingFailure, HCursor}
import li.flxkbr.schema.generator.*
import cats.implicits.toTraverseOps

object Codecs {

  import BasicTypeCodecSupport.given

  object SchemaFields {
    val Name: String  = "_name"
    val Count: String = "_count"

    val reservedFields: Set[String] = Set(Name, Count)
  }

  object ColumnFields {
    val Type: String       = "type"
    val PrimaryKey: String = "p_key"
  }

  given columnDecoder: Decoder[Column] = new Decoder[Column] {
    override final def apply(c: HCursor): Result[Column] = {
      for {
        genType <- c.downField("type").as[String].flatMap { t =>
          GeneratorType.ofValue(t).toRight(DecodingFailure(s"Invalid generator type $t", c.history))
        }
        column <- c.as[Column](using genType.columnDecoder)
      } yield column
    }
  }

  given schemaDecoder: Decoder[Schema] = new Decoder[Schema] {
    override final def apply(c: HCursor): Result[Schema] = {
      for {
        name  <- c.get[SchemaName]("_name")
        count <- c.get[Int]("_count")
        columns <- c.keys.get.toList
          .filterNot(Codecs.SchemaFields.reservedFields)
          .map { key =>
            c.get[Column](key)
          }
          .sequence
      } yield Schema(name, count, columns)
    }
  }
}
