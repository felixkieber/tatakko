package li.flxkbr.schema.generator

import cats.MonadError
import scala.util.Try
import cats.Show

final case class ColumnPath(schema: SchemaName, column: ColumnName) {
  override def toString(): String = s"$schema.$column"
}

object ColumnPath {
  given Show[ColumnPath] = Show.show(cp => s"${cp.schema}.${cp.column}")
  
  def safe[F[_]](path: String)(using me: MonadError[F, Throwable]): F[ColumnPath] = {
    path.split("[.:/]") match {
      case Array(table, column) => me.pure(ColumnPath(SchemaName.convert(table), ColumnName.convert(column)))
      case _                    => me.raiseError(InvalidColumnPathException(path))
    }
  }

  def must(path: String): ColumnPath = safe[Try](path).get

  case class InvalidColumnPathException(path: String) extends Exception(s"Invalid column path `$path` specified.")
}

extension (s: String) def pathSafe: String = s.replaceAll("[.:/-]", "_")
