package li.flxkbr.schema

import cats.Monad
import cats.MonadError
import cats.MonadError.apply
import cats.implicits.catsSyntaxEither
import cats.implicits.toFlatMapOps
import cats.implicits.toFunctorOps
import io.circe.DecodingFailure
import io.circe.ParsingFailure
import io.circe.parser.{parse => cParse}
import li.flxkbr.schema.generator.Schema

import java.io.File
import java.net.URI
import java.nio.file.Files
import scala.io.Source
import scala.io.{Codec => ScalaCodec}
import cats.Show

class SchemaParser {

  import li.flxkbr.schema.codec.Codecs.given

  def parse[F[_]](path: String)(using me: MonadError[F, Throwable]): F[Schema] = {

    for {
      jsonString <- me.catchNonFatal(Source.fromFile(path)(ScalaCodec.UTF8).getLines.mkString(System.lineSeparator))
      json <- cParse(jsonString).liftTo
      decoded <- json.as[Schema].liftTo
    } yield decoded

  }
}
