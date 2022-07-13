package li.flxkbr.schema.codec

import io.circe.*
import io.circe.parser.*
import li.flxkbr.schema.codec.BasicTypeCodecSupport
import li.flxkbr.schema.generator.*
import li.flxkbr.testutil.CustomMatcherSupport
import li.flxkbr.testutil.UnitSpec
import org.scalatest.EitherValues

import java.time.Instant

import concurrent.duration.DurationInt

class ParameterDecodersSpec extends UnitSpec {

  "intParametersDecoder" should "decode valid parameters" in {
    val parsedJsons = Seq(
      """
      {
        "parameters": {
          "max": 1000,
          "min": 10
        }
      }
    """,
      """
      {
        "parameters": {
          "min": 100
        }    
      }
    """,
      """
      {
        "parameters": {
          "max": 6
        }
      }
    """,
      """
      {
        "parameters": {}
      }
    """,
    ).map(parse)

    assume(parsedJsons.forall(_.isRight))

    val json1 :: json2 :: json3 :: json4 :: _ = parsedJsons.map(_.right.value)

    given Decoder[IntParameters] = ParameterDecoders.intParametersDecoder

    json1.hcursor.get[IntParameters]("parameters").right.value should be(IntParameters(10, 1000))
    json2.hcursor.get[IntParameters]("parameters").right.value should be(IntParameters(100, Int.MaxValue))
    json3.hcursor.get[IntParameters]("parameters").right.value should be(IntParameters(0, 6))
    json4.hcursor.get[IntParameters]("parameters").right.value should be(IntParameters(0, Int.MaxValue))
  }
}
