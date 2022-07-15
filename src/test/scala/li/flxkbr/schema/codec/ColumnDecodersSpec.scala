package li.flxkbr.schema.codec

import li.flxkbr.testutil.UnitSpec
import io.circe.*, io.circe.parser.*
import li.flxkbr.schema.generator.*

class ColumnDecodersSpec extends UnitSpec {

  "noParametersColumnDecoder" should "decode any valid column" in {
    val parsedJson = parse("""
            {
                "column1": {
                    "type": "serial",
                    "p_key": true
                },
                "column2": {
                    "type": "serial"
                },
                "column3": {
                    "type": "bumblo",
                    "parameters": {},
                    "p_key": false
                }
            }
            """)

    assume(parsedJson.isRight)

    val json = parsedJson.value

    val serialDecoder = ColumnDecoders.noParametersColumnDecoder(GeneratorType.Serial)

    val customGenType = new GeneratorType("bumblo") {
      def columnDecoder: Decoder[Column] = ???
    }

    json.hcursor.get[Column]("column1")(using serialDecoder).value should be(
      Column(ColumnName.convert("column1"), GeneratorType.Serial, NoParameters, true)
    )
    json.hcursor.get[Column]("column2")(using serialDecoder).value should be(
      Column(ColumnName.convert("column2"), GeneratorType.Serial, NoParameters, false)
    )
    json.hcursor
      .get[Column]("column3")(using ColumnDecoders.noParametersColumnDecoder(customGenType))
      .value should be(
      Column(ColumnName.convert("column3"), customGenType, NoParameters, false)
    )
  }

  "intColumnDecoder" should "decode valid int columns" in {
    val parsedJson = parse("""
        {
            "intcol1": {
                "type": "int",
                "parameters": {
                    "min": 100,
                    "max": 1000
                }
            },
            "intcol2": {
                "type": "int",
                "parameters": {
                    "min": 5
                },
                "p_key": true
            },
            "intcol3": {
                "type": "int",
                "parameters": {
                    "max": 1023
                }
            },
            "intcol4": {
                "type": "int",
                "parameters": {}
            },
            "intcol5": {
                "type": "int"
            }
        }
        """)

    assume(parsedJson.isRight)

    val json = parsedJson.value

    given Decoder[Column] = ColumnDecoders.intColumnDecoder

    json.hcursor.get[Column]("intcol1").value should be(
      Column(ColumnName.convert("intcol1"), GeneratorType.Int, IntParameters(100, 1000), false)
    )
    json.hcursor.get[Column]("intcol2").value should be(
      Column(ColumnName.convert("intcol2"), GeneratorType.Int, IntParameters(5, Int.MaxValue), true)
    )
    json.hcursor.get[Column]("intcol3").value should be(
      Column(ColumnName.convert("intcol3"), GeneratorType.Int, IntParameters(0, 1023), false)
    )
    json.hcursor.get[Column]("intcol4").value should be(
      Column(ColumnName.convert("intcol4"), GeneratorType.Int, IntParameters.Defaults, false)
    )
    json.hcursor.get[Column]("intcol5").value should be(
      Column(ColumnName.convert("intcol5"), GeneratorType.Int, IntParameters.Defaults, false)
    )
  }
}
