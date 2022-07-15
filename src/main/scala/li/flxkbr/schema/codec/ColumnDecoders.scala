package li.flxkbr.schema.codec

import io.circe.CursorOp
import io.circe.Decoder
import io.circe.DecodingFailure
import io.circe.HCursor
import li.flxkbr.schema.generator.*
import scala.util.Try

object ColumnDecoders {

  private case class CommonFields(name: ColumnName, primaryKey: Boolean)

  import BasicTypeCodecs.columnPathDecoder
  import ParameterDecoders.given
  import Codecs.ColumnFields.*

  protected def assertType(
      cursor: HCursor,
      t: GeneratorType,
  ): Either[DecodingFailure, Unit] = {
    cursor.get[String]("type").flatMap {
      case t.value => Right(())
      case invalid =>
        Left(DecodingFailure(s"Parsed generator type $invalid does not correspond to expected type $t", cursor.history))
    }
  }

  def noParametersColumnDecoder(genType: GeneratorType): Decoder[Column] = Decoder.instance { cursor =>
    for {
      _      <- assertType(cursor, genType)
      common <- retrieveCommonFields(cursor)
    } yield Column(common.name, genType, NoParameters, common.primaryKey)
  }

  val intColumnDecoder: Decoder[Column] = Decoder.instance { cursor =>
    for {
      _             <- assertType(cursor, GeneratorType.Int)
      common        <- retrieveCommonFields(cursor)
      intParameters <- cursor.getOrElse[IntParameters]("parameters")(IntParameters.Defaults)
    } yield Column(common.name, GeneratorType.Int, intParameters, common.primaryKey)
  }

  val referenceColumnDecoder: Decoder[Column] = Decoder.instance { cursor =>
    for {
      _         <- assertType(cursor, GeneratorType.Reference)
      common    <- retrieveCommonFields(cursor)
      reference <- cursor.get[ColumnPath]("target")
    } yield Column(common.name, GeneratorType.Reference, ReferenceParameters(reference), common.primaryKey)
  }

  private def retrieveCommonFields(cursor: HCursor): Either[DecodingFailure, CommonFields] = {
    for {
      name <- cursor.key.toRight(DecodingFailure("Could not retrieve key of column object", cursor.history))
      pkey <- cursor.getOrElse[Boolean](PrimaryKey)(false)
    } yield CommonFields(ColumnName.convert(name), pkey)
  }
}
