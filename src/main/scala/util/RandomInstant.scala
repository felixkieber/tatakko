package util

import java.time.Instant
import scala.util.Random

case class RandomInstant(start: Instant, end: Instant) {
  private val _start = start.getEpochSecond
  private val _diff  = end.getEpochSecond - _start

  def get: Instant = {
    Instant.ofEpochSecond(_start + Random.nextLong(_diff))
  }

}
