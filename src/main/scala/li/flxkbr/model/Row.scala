package li.flxkbr
package model

type Raw = Seq[Any]

trait Row {
  def asRaw: Raw
}

trait ProductRow extends Row with Product {
  def asRaw: Raw = {
    productIterator.toSeq
  }
}

case class RawRow(raw: Raw) extends Row {
  override def asRaw: Raw = raw
}
