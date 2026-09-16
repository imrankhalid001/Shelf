package com.shelf.domain.usecase

import com.shelf.domain.model.Book
import com.shelf.domain.model.ReadingStatus
import com.shelf.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow

class GetLibraryUseCase(
    private val bookRepository: BookRepository
) {
    fun observeAll(): Flow<List<Book>> = bookRepository.observeLibrary()

    fun observeByStatus(status: ReadingStatus): Flow<List<Book>> {
        return bookRepository.observeLibraryByStatus(status)
    }
}
