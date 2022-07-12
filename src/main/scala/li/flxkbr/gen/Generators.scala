package li.flxkbr
package gen

import model.*
import util.{GenSupport, PDecider, RandomInstant}

import java.time.Instant
import java.time.temporal.ChronoUnit
import scala.util.Random

class UserGenerator() extends RowGenerator[User] with GenSupport {
  override val key = "user"

  override val headerRow: Seq[String] = Seq(
    "id",
    "global_user_id",
    "event_tracking_id",
    "country_id",
    "department",
    "is_system",
    "deleted_ts",
    "effective_ts",
    "expired_ts",
    "created_ts",
    "first_seen_ts",
  )

  val departments = IndexedSeq("Hell", "Heaven", "Midgard", null)
  val countries   = IndexedSeq("ch", "de", "us", null)

  val id: () => Int = createIntSequence

  val createdTs = Instant.parse("2019-01-05T10:15:30.00Z")

  val randomCreatedTs = RandomInstant(createdTs, createdTs.plus(30, ChronoUnit.DAYS))

  override def nextRow: User = {
    val created       = randomCreatedTs.get
    val createdString = created.toString
    User(
      id(),
      uuid(),
      uuid(),
      countries.pickAny,
      departments.pickAny,
      false,
      null,
      effectiveTs = created,
      expiredTs = Instant.MAX,
      createdTs = created,
      firstSeenTs = created.randomAfter(7 * 24, ChronoUnit.HOURS),
    )
  }
}

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
