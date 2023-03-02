package li.flxkbr.model.codec

import io.circe.Decoder
import cats.MonadThrow
import li.flxkbr.model.ModelField
import li.flxkbr.model.codec.ModelFieldRegistry.DuplicateDecoderIdentifier
import io.circe.HCursor
import io.circe.Decoder.Result

class ModelFieldRegistry(_registry: Map[DecoderIdentifier, Decoder[ModelField]] = Map.empty) {

  def updated[F[_]](
      codec: (DecoderIdentifier, Decoder[ModelField]),
      codecs: (DecoderIdentifier, Decoder[ModelField])*
  )(using me: MonadThrow[F]): F[ModelFieldRegistry] =
    val allCodecs   = codec +: codecs
    val identifiers = allCodecs.map(_._1)
    val overlaps =
      identifiers.diff(identifiers.distinct) ++ identifiers.intersect(_registry.keys.toSeq)
    if overlaps.nonEmpty then me.raiseError(DuplicateDecoderIdentifier(overlaps))
    else
      me.pure(new ModelFieldRegistry(_registry ++ allCodecs))

  implicit val decoder: Decoder[ModelField] = new Decoder[ModelField] {

    override def apply(c: HCursor): Result[ModelField] = ???

  }
}

object ModelFieldRegistry {
  case class DuplicateDecoderIdentifier(identifiers: Seq[DecoderIdentifier])
      extends Exception(s"Tried to add duplicate decoder identifiers: ${identifiers.mkString(",")}")
}

case class DecoderIdentifier(dataType: String, generatorId: String)
