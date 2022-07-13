package li.flxkbr
package testutil

import org.scalatest._
import flatspec._
import matchers._

abstract class UnitSpec
    extends AnyFlatSpec
    with should.Matchers
    with OptionValues
    with EitherValues
    with Inside
    with Inspectors
    with CustomMatcherSupport
