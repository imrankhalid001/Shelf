package com.shelf.domain.usecase

import com.shelf.core.result.AppResult
import com.shelf.domain.model.Book
import com.shelf.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow

class GetBookDetailsUseCase(
    private val bookRepository: BookRepository
) {
    operator fun invoke(bookId: String): Flow<AppResult<Book>> {
        return bookRepository.observeBookDetails(bookId)
    }
}
