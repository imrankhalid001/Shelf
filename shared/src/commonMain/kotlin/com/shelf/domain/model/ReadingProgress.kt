package com.shelf.domain.model

data class ReadingProgress(
    val bookId: String,
    val currentPage: Int = 0,
    val totalPages: Int = 0,
    val percentage: Double = 0.0,
    val status: ReadingStatus = ReadingStatus.WANT_TO_READ,
    val startedAt: Long? = null,
    val finishedAt: Long? = null,
    val updatedAt: Long = 0L
)
