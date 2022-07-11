package li.flxkbr
package gen

import model.{Raw, Row}

trait RowGenerator[T <: Row | Raw] {

  val key: String
  val headerRow: Seq[String]

  def nextRow: T
  def bulk(n: Int): Seq[T] = {
    for _ <- 1 to n yield nextRow
  }
}
