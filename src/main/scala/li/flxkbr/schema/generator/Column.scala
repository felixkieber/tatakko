package li.flxkbr
package schema.generator

opaque type ColumnName = String
object ColumnName {
  def convert(name: String): ColumnName = name.pathSafe
}

class Column(
    val name: ColumnName,
    val genType: GeneratorType,
    val parameters: ColumnParameters,
)
