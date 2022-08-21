package li.flxkbr.gen.generators

import li.flxkbr.gen.RowGenerator
import li.flxkbr.model.Question
import li.flxkbr.model.User
import li.flxkbr.util.GenSupport
import li.flxkbr.util.PDecider
import li.flxkbr.util.RandomInstant

import java.time.Instant
import java.time.temporal.ChronoUnit
import scala.util.Random

class QuestionGenerator(users: IndexedSeq[User], preloadedP: Float = 0.001, deletedP: Float = 0.001)
    extends RowGenerator[Question]
    with GenSupport {
  override val key: String = "question"
  override val headerRow: Seq[String] = Seq(
    "id",
    "source_id",
    "user_id",
    "language_id",
    "category_id",
    "knowledge_space_id",
    "is_preloaded",
    "upvote_count",
    "downvote_count",
    "published_ts",
    "deleted_ts",
    "effective_ts",
    "expired_ts",
  )

  val id               = createIntSequence
  val preloadedDecider = new PDecider(preloadedP)
  val deletedDecider   = new PDecider(deletedP)

  override def nextRow: Question = {
    val author = users.pickAny
    val qid    = id()
    val pubTs  = author.firstSeenTs.randomAfter(30, ChronoUnit.DAYS)

    Question(
      qid,
      qid,
      author.id,
      "en",
      null,
      null,
      preloadedDecider.decide,
      Random.nextInt(10),
      Random.nextInt(4),
      pubTs,
      if (deletedDecider.decide) pubTs.randomAfter(30, ChronoUnit.DAYS) else null,
      effectiveTs = pubTs,
      expiredTs = Instant.MAX,
    )
  }

}