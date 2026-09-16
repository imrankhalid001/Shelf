package com.shelf.domain.usecase

import com.shelf.core.result.AppResult
import com.shelf.domain.model.Book
import com.shelf.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow

class SearchBooksUseCase(
    private val bookRepository: BookRepository
) {
    operator fun invoke(query: String, page: Int = 1): Flow<AppResult<List<Book>>> {
        return bookRepository.searchBooks(query, page)
    }
}
