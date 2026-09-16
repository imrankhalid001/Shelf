package com.shelf.domain.model

data class ReadingSession(
    val id: String,
    val bookId: String,
    val startedAt: Long,
    val endedAt: Long,
    val durationSeconds: Long,
    val pagesRead: Int
)
