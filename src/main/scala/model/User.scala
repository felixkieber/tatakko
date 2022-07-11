package li.flxkbr
package model

import java.time.Instant
import java.util.UUID

case class User(
    id: Int,
    globalUserId: UUID,
    eventTrackingId: UUID,
    countryId: String,
    department: String,
    isSystem: Boolean,
    deletedTs: Instant,
    effectiveTs: Instant,
    expiredTs: Instant,
    createdTs: Instant,
    firstSeenTs: Instant,
) extends ProductRow
