package li.flxkbr.gen.generators

import li.flxkbr.gen.*
import li.flxkbr.model.User
import li.flxkbr.util.GenSupport
import li.flxkbr.util.RandomInstant

import java.time.Instant
import java.time.temporal.ChronoUnit

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
