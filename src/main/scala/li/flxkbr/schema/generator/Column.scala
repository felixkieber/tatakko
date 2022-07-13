package li.flxkbr.schema.generator

opaque type ColumnName = String
object ColumnName {
  def convert(name: String): ColumnName = name.pathSafe
}

case class Column(
    name: ColumnName,
    genType: GeneratorType,
    parameters: ColumnParameters,
    primaryKey: Boolean,
)
