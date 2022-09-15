package li.flxkbr
package model

type Raw = Seq[Any]

trait Row {
  def asRaw: Raw

  def at[A](i: Int): A
}

trait ProductRow extends Row {
  this: Product =>
  def asRaw: Raw = {
    productIterator.toSeq
  }

  def at[A](i: Int): A = this.productElement(i).asInstanceOf[A]
}

case class RawRow(raw: Raw) extends Row {
  override def asRaw: Raw = raw
  override def at[A](i: Int): A = raw(i).asInstanceOf[A]
}
