package li.flxkbr.schema.codec

import li.flxkbr.testutil.UnitSpec
import java.time.Instant
import io.circe.*, io.circe.parser.*
import concurrent.duration.DurationInt

class BasicTypeCodecSupportSpec extends UnitSpec {

  "instantDecoder" should "decode valid instants" in {
    val now = Instant.now()
    val rawJson = s"""
    {
      "time": "${now.toString}"
    }
    """

    val parsed = parse(rawJson)
    assume(parsed.isRight)

    val json = parse(rawJson).value

    json.hcursor.get[Instant]("time")(using BasicTypeCodecs.instantDecoder).right.value should be(now)
  }

  it should "decode special keyword `now`" in {
    val rawJson = s"""
    {
      "time": "now"
    }
    """
    val parsed = parse(rawJson)
    assume(parsed.isRight)

    val json = parse(rawJson).value

    json.hcursor.get[Instant]("time")(using BasicTypeCodecs.instantDecoder).right.value should beInstantWithin(
      20.millis,
      Instant.now(),
    )
  }
}
