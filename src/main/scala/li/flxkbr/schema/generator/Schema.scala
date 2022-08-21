package li.flxkbr.schema.generator

opaque type SchemaName = String

object SchemaName {
  def convert(name: String): SchemaName = name.pathSafe
}

case class Schema(
    name: SchemaName,
    count: Int,
    columns: Seq[Column],
)
