package li.flxkbr
package schema.generator

import schema.*

opaque type SchemaName = String
object SchemaName {
  def convert(name: String): SchemaName = name.pathSafe
}
class Schema(
    name: SchemaName,
    count: Int,
    columns: Seq[Column[GeneratorType]],
)

opaque type ColumnName = String
object ColumnName {
  def convert(name: String): ColumnName = name.pathSafe
}

class Column[T <: GeneratorType](
    name: ColumnName,
    `type`: T,
    parameters: `type`.P,
)

final case class ColumnPath(schema: SchemaName, column: ColumnName)

object ColumnPath {
  def safe(path: String): Option[ColumnPath] = {
    path.split("[.:/]") match {
      case Array(table, column) => Some(ColumnPath(SchemaName.convert(table), ColumnName.convert(column)))
      case _                    => None
    }
  }

  def must(path: String): ColumnPath = {
    safe(path).getOrElse(throw InvalidColumnPathException(path))
  }
}

extension (s: String) def pathSafe: String = s.replaceAll("[.:/-]", "_")

class InvalidColumnPathException(path: String) extends Exception
