package li.flxkbr.schema.generator

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
