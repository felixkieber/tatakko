package li.flxkbr
package util

import java.time.Instant
import java.time.temporal.TemporalUnit
import java.util.UUID
import scala.util.Random

trait GenSupport {

  extension [A](is: IndexedSeq[A])
    def pickAny: A = {
      is(Random.nextInt(is.size))
    }

  extension (i: Instant)
    def randomAfter(range: Int, unit: TemporalUnit): Instant = {
      i.plus(1 + Random.nextInt(range), unit)
    }

  def createIntSequence: () => Int = {
    var i = 0;
    () => {
      i += 1
      i
    }
  }

  def uuid(): UUID = UUID.randomUUID()
}
