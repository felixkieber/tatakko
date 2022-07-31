package li.flxkbr.schema.generator

import cats.MonadError
import cats.implicits.catsSyntaxMonadError
import cats.implicits.toFunctorOps
import scala.util.Try

sealed trait Cardinality

object Cardinality {
  case class AtMost private[Cardinality] (n: Int) extends Cardinality
  case object Full                                extends Cardinality

  def safe[F[_]](cardinality: Int | String)(using me: MonadError[F, Throwable]): F[Cardinality] =
    cardinality match {
      case s: String if s.toLowerCase == "full" => me.pure(Full)
      case s: String => me.catchNonFatal(s.toInt).ensureOr(InvalidCardinalityException.apply)(_ > 0).map(AtMost.apply)
      case i: Int if i > 0 => me.pure(AtMost(i))
      case invalid         => me.raiseError(InvalidCardinalityException(invalid))
    }

  def must(cardinality: Int | String): Cardinality = safe[Try](cardinality).get

  case class InvalidCardinalityException(param: Int | String)
      extends Exception(s"Unable to parse cardinality parameter `$param`")
}
