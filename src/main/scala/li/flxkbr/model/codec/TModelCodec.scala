package li.flxkbr.model.codec

import io.circe.Codec
import li.flxkbr.model.TModel
import io.circe.Decoder
import io.circe.HCursor
import io.circe.Decoder.Result
import li.flxkbr.model.ModelName
import io.circe.DecodingFailure
import io.circe.DecodingFailure.Reason
import cats.implicits.toTraverseOps
import li.flxkbr.model.ModelField

object TModelCodec {
  given decoder(using ModelFieldRegistry): Decoder[TModel] = new Decoder[TModel] {
    given Decoder[ModelField] = summon[ModelFieldRegistry].decoder
    override def apply(c: HCursor): Result[TModel] =
      for
        name <- c.downField("_name").as[ModelName]
        keys <- c.keys.toRight(DecodingFailure(Reason.MissingField, c.history))
        _ <- keys.toList.traverse { key =>
          c.downField(key).as[ModelField]
        }
      yield new TModel(name, Set.empty, Seq.empty)

  }
}
