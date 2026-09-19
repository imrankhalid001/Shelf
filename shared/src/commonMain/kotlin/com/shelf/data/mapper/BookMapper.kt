package com.shelf.data.mapper

import com.shelf.core.utils.DateUtils
import com.shelf.data.local.entity.BookEntity
import com.shelf.data.local.entity.NoteEntity
import com.shelf.data.local.entity.QuoteEntity
import com.shelf.data.local.entity.ReadingGoalEntity
import com.shelf.data.local.entity.ReadingProgressEntity
import com.shelf.data.local.entity.ReadingSessionEntity
import com.shelf.data.remote.dto.SearchBookDocDto
import com.shelf.domain.model.Book
import com.shelf.domain.model.Note
import com.shelf.domain.model.Quote
import com.shelf.domain.model.ReadingGoal
import com.shelf.domain.model.ReadingProgress
import com.shelf.domain.model.ReadingSession
import com.shelf.domain.model.ReadingStatus

fun SearchBookDocDto.toBookEntity(): BookEntity {
    val cleanId = key.removePrefix("/works/").removePrefix("works/").trim()
    val now = DateUtils.nowEpochMillis()
    return BookEntity(
        id = cleanId,
        workId = cleanId,
        title = title,
        coverId = coverId,
        coverUrl = coverId?.let { "https://covers.openlibrary.org/b/id/$it-M.jpg" },
        firstPublishYear = firstPublishYear,
        pageCount = 0,
        isbn10 = isbns.firstOrNull { it.length == 10 },
        isbn13 = isbns.firstOrNull { it.length == 13 },
        subjects = subjects.take(5).joinToString(", "),
        createdAt = now,
        updatedAt = now
    )
}

fun BookEntity.toDomain(progress: ReadingProgress? = null, isFavorite: Boolean = false): Book {
    val effectivePageCount = if (pageCount > 0) {
        pageCount
    } else if ((progress?.totalPages ?: 0) > 0) {
        progress!!.totalPages
    } else {
        100
    }
    return Book(
        id = id,
        workId = workId,
        title = title,
        subtitle = subtitle,
        description = description,
        coverId = coverId,
        coverUrl = coverUrl,
        firstPublishYear = firstPublishYear,
        pageCount = effectivePageCount,
        isbn10 = isbn10,
        isbn13 = isbn13,
        subjects = subjects.split(", ").filter { it.isNotBlank() },
        publisher = publisher,
        language = language,
        readingProgress = progress,
        isFavorite = isFavorite,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun ReadingProgressEntity.toDomain(): ReadingProgress {
    return ReadingProgress(
        bookId = bookId,
        currentPage = currentPage,
        totalPages = totalPages,
        percentage = percentage,
        status = ReadingStatus.fromString(status),
        startedAt = startedAt,
        finishedAt = finishedAt,
        updatedAt = updatedAt
    )
}

fun ReadingProgress.toEntity(): ReadingProgressEntity {
    return ReadingProgressEntity(
        bookId = bookId,
        currentPage = currentPage,
        totalPages = totalPages,
        percentage = percentage,
        status = status.name,
        startedAt = startedAt,
        finishedAt = finishedAt,
        updatedAt = updatedAt
    )
}

fun NoteEntity.toDomain(): Note {
    return Note(
        id = id,
        bookId = bookId,
        content = content,
        pageNumber = pageNumber,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun QuoteEntity.toDomain(): Quote {
    return Quote(
        id = id,
        bookId = bookId,
        text = text,
        pageNumber = pageNumber,
        createdAt = createdAt
    )
}

fun ReadingSessionEntity.toDomain(): ReadingSession {
    return ReadingSession(
        id = id,
        bookId = bookId,
        startedAt = startedAt,
        endedAt = endedAt,
        durationSeconds = durationSeconds,
        pagesRead = pagesRead
    )
}

fun ReadingGoalEntity.toDomain(): ReadingGoal {
    val percentage = if (targetBooks > 0) (completedBooks.toDouble() / targetBooks) * 100.0 else 0.0
    return ReadingGoal(
        id = id,
        year = year,
        targetBooks = targetBooks,
        completedBooks = completedBooks,
        progressPercentage = percentage
    )
}
