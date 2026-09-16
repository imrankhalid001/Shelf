package com.shelf.domain.usecase

import com.shelf.core.error.AppError
import com.shelf.core.result.AppResult
import com.shelf.domain.repository.BookRepository

class UpdateReadingProgressUseCase(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(bookId: String, currentPage: Int, totalPages: Int): AppResult<Unit> {
        if (currentPage < 0) {
            return AppResult.Error(AppError.Validation.InvalidPageNumber(totalPages))
        }
        return bookRepository.updateReadingProgress(bookId, currentPage, totalPages)
    }
}
