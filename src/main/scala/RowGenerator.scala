import model.{Raw, RawRow, Row}

trait RowGenerator[T <: Row | Raw] {

  val key: String
  val headerRow: Seq[String]

  def nextRow: T
  def bulk(n: Int): Seq[T] = {
    for _ <- 1 to n yield nextRow
  }
}
