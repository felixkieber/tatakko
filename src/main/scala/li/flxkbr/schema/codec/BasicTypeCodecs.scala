package li.flxkbr.schema.codec

import cats.implicits.catsStdInstancesForTry
import io.circe.Decoder
import io.circe.DecodingFailure
import li.flxkbr.schema.generator.Cardinality
import li.flxkbr.schema.generator.ColumnName
import li.flxkbr.schema.generator.ColumnPath
import li.flxkbr.schema.generator.SchemaName

import java.time.Instant
import scala.util.Try

object BasicTypeCodecs {
  given instantDecoder: Decoder[Instant] = Decoder.instance { c =>
    c.as[String].flatMap {
      case "now" => Right(Instant.now())
      case time  => Try(Instant.parse(time)).toEither.left.map(ex => DecodingFailure.fromThrowable(ex, c.history))
    }
  }

  given columnNameDecoder: Decoder[ColumnName] = Decoder.decodeString.map(ColumnName.convert)

  given schemaNameDecoder: Decoder[SchemaName] = Decoder.decodeString.map(SchemaName.convert)

  given columnPathDecoder: Decoder[ColumnPath] = Decoder.decodeString.emapTry(ColumnPath.safe)

  given cardinalityDecoder: Decoder[Cardinality] = Decoder.decodeString.emapTry(Cardinality.safe)
}