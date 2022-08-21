package li.flxkbr.gen.generators

import li.flxkbr.gen.RowGenerator
import li.flxkbr.util.GenSupport
import li.flxkbr.model.User
import li.flxkbr.model.Question
import li.flxkbr.model.Raw
import java.time.Instant
import li.flxkbr.util.RandomInstant
import java.time.temporal.ChronoUnit


class SetupQuestionViewGenerator(users: IndexedSeq[User], questions: IndexedSeq[Question])
    extends RowGenerator[Raw]
    with GenSupport {
  override val key: String = "setup-question-views"
  override val headerRow: Seq[String] = Seq(
    "id",
    "question_id",
    "user_id",
    "created_ts",
    "loaded_ts",
  )

  val id = createIntSequence

  val createdStart = Instant.parse("2020-01-05T10:15:30.00Z")

  val randomCreated = RandomInstant(createdStart, createdStart.plus(30, ChronoUnit.DAYS))
  val loadedTs      = Instant.parse("2022-01-05T10:15:30.00Z").toString

  override def nextRow: Raw = {
    Seq(
      id(),
      questions.pickAny.sourceId,
      users.pickAny.globalUserId,
      randomCreated.get.toString,
      loadedTs,
    )
  }
}