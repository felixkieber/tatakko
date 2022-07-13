package li.flxkbr.testutil

import java.time.Instant
import scala.concurrent.duration.{FiniteDuration, DurationInt, MICROSECONDS, NANOSECONDS}
import org.scalatest.matchers.Matcher
import org.scalatest.matchers.MatchResult
import org.scalatest.exceptions.TestCanceledException
import org.scalatest.Assertions.cancel

trait CustomMatcherSupport {

  class InstantIsWithin private[CustomMatcherSupport] (margin: FiniteDuration, reference: Instant)
      extends Matcher[Instant] {
    def apply(result: Instant) = {
      margin.unit match {
        case MICROSECONDS | NANOSECONDS =>
          cancel(s"Invalid resolution of margin `$margin`, MICROSECOND and NANOSECOND not supported")
        case _ => ()
      }
      val delta   = margin.toMillis
      val absDiff = Math.abs(result.toEpochMilli() - reference.toEpochMilli())
      MatchResult(
        absDiff <= delta,
        s"""Instant $result has $absDiff ms difference from reference $reference, exceeds delta $delta""",
        s"""Instant $result has $absDiff ms difference from reference $reference, within delta $delta""",
      )
    }
  }

  def beInstantWithin(margin: FiniteDuration = 100.milliseconds, reference: Instant = Instant.now()) =
    InstantIsWithin(margin, reference)
}
