package li.flxkbr.schema

import li.flxkbr.testutil.UnitSpec
import java.net.URI
import li.flxkbr.schema.generator.*
import java.time.Instant

class SchemaParserSpec extends UnitSpec {
  
    "SchemaParser" should "correctly parse the sample file" in {
        val parser = SchemaParser()

        val testFile = getClass.getResource("/sample-schema.json").getPath

        val parsed: Either[Throwable, Schema] = parser.parse(testFile)

        parsed.right.value shouldBe a [Schema]

        inside (parsed.right.value) { case Schema(name, count, cols) =>
            name shouldBe SchemaName.convert("players")
            count shouldBe 500
            cols should contain allOf(
                Column(
                    ColumnName.convert("id"),
                    GeneratorType.Serial,
                    NoParameters,
                    primaryKey = true,
                ),
                Column(
                    ColumnName.convert("name"),
                    GeneratorType.String,
                    StringParameters(
                        domain = Some("person-name"),
                        cardinality = Cardinality.Full,
                    ),
                    primaryKey = false,
                ),
                Column(
                    ColumnName.convert("age"),
                    GeneratorType.Int,
                    IntParameters(
                        min = 15,
                        max = 69,
                    ),
                    primaryKey = false,
                ),
                Column(
                    ColumnName.convert("joined_ts"),
                    GeneratorType.Instant,
                    InstantParameters(
                        start = Instant.parse("2012-01-01T00:00:00.000Z"),
                        end = Instant.parse("2012-06-01T00:00:00.000Z"),
                    ),
                    primaryKey = false,
                ),
                Column(
                    ColumnName.convert("country"),
                    GeneratorType.Reference,
                    ReferenceParameters(
                        target = ColumnPath.must("country.id")
                    ),
                    primaryKey = false,
                )
            )
        }
    }
}
