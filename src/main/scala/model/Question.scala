package model

import java.time.Instant

case class Question(
    id: Int,
    sourceId: Int,
    userId: Int,
    languageId: String,
    categoryId: Integer,
    knowledgeSpaceId: Integer,
    isPreloaded: Boolean,
    upvoteCount: Int,
    downvoteCount: Int,
    publishedTs: Instant,
    deletedTs: Instant,
    effectiveTs: Instant,
    expiredTs: Instant,
) extends ProductRow
