package li.flxkbr
package schema.generator

import testutil.UnitSpec
import io.circe.*, io.circe.parser.*
import org.scalatest.EitherValues

class ParameterDecodersSpec extends UnitSpec with EitherValues {

  "noParametersDecoder" should "decode any input" in {
    val jsons = Seq(
      "{}", "", "[]", "something"
    )
    given decoder: Decoder[NoParameters] = ParameterDecoders.noParametersDecoder

    forEvery (jsons) { json =>
      decode[NoParameters](json).right.value should be (NoParameters)
    }
  }
}
