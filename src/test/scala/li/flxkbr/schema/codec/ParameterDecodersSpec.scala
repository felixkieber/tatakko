package li.flxkbr.schema.codec

import cats.implicits.catsSyntaxOptionId
import io.circe.*
import io.circe.parser.*
import li.flxkbr.schema.codec.BasicTypeCodecs
import li.flxkbr.schema.generator.*
import li.flxkbr.testutil.*
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

  "stringParametersDecoder" should "decode valid parameters" in {
    val jsonAndResults = List(
      (
        """{
        "parameters": {
          "domain": "my-domain",
          "cardinality": "full"
        }
      }
      """,
        StringParameters("my-domain".some, Cardinality.Full),
      ),
      (
        """{
        "parameters": {
          "cardinality": "12"
        }
      }
      """,
        StringParameters(None, Cardinality.must(12)),
      ),
      (
        """{
        "parameters": {
          "cardinality": 19
        }
      }
      """,
        StringParameters(None, Cardinality.must(19)),
      ),
      (
        """{
        "parameters": {}
      }
      """,
        StringParameters(None, Cardinality.Full),
      ),
    ).map { case (jsonString, expected) =>
       (parse(jsonString), expected)
    }

    assume(jsonAndResults.forall(_._1.isRight))

    
  }
}
