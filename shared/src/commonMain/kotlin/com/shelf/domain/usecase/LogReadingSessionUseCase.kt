package com.shelf.domain.usecase

import com.shelf.core.error.AppError
import com.shelf.core.result.AppResult
import com.shelf.domain.repository.BookRepository
import com.shelf.domain.repository.StatsRepository

class LogReadingSessionUseCase(
    private val statsRepository: StatsRepository,
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(
        bookId: String,
        durationSeconds: Long,
        startPage: Int,
        endPage: Int,
        totalPages: Int
    ): AppResult<Unit> {
        if (durationSeconds <= 0) {
            return AppResult.Error(AppError.Validation.Custom("Duration must be greater than zero."))
        }
        if (endPage < startPage) {
            return AppResult.Error(AppError.Validation.Custom("End page cannot be less than start page."))
        }

        val pagesRead = endPage - startPage
        
        val sessionResult = statsRepository.logReadingSession(bookId, durationSeconds, pagesRead)
        if (sessionResult is AppResult.Error) return sessionResult

        return bookRepository.updateReadingProgress(bookId, endPage, totalPages)
    }
}
