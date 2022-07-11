package li.flxkbr.util

import scala.util.Random

class PDecider(trueP: Float = 0.5f) {
  def decide: Boolean = Random.nextFloat() < trueP
}
