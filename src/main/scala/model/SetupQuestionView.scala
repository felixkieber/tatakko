package model

import java.time.Instant

case class SetupQuestionView(
    id: Int,
    questionId: Int,
    userId: Int,
    createdTs: Instant,
    loadedTs: Instant,
) extends ProductRow
