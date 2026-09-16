package com.shelf.domain.repository

import com.shelf.core.result.AppResult
import com.shelf.domain.model.Book
import com.shelf.domain.model.Note
import com.shelf.domain.model.Quote
import com.shelf.domain.model.ReadingProgress
import com.shelf.domain.model.ReadingStatus
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun searchBooks(query: String, page: Int = 1): Flow<AppResult<List<Book>>>
    fun observeLibrary(): Flow<List<Book>>
    fun observeLibraryByStatus(status: ReadingStatus): Flow<List<Book>>
    fun observeBookDetails(id: String): Flow<AppResult<Book>>
    suspend fun saveBook(book: Book): AppResult<Unit>
    suspend fun updateReadingProgress(bookId: String, currentPage: Int, totalPages: Int): AppResult<Unit>
    suspend fun updateReadingStatus(bookId: String, status: ReadingStatus): AppResult<Unit>
    suspend fun deleteBook(id: String): AppResult<Unit>
    fun observeNotes(bookId: String): Flow<List<Note>>
    suspend fun addNote(bookId: String, content: String, pageNumber: Int?): AppResult<Unit>
    suspend fun deleteNote(noteId: String): AppResult<Unit>
    fun observeQuotes(bookId: String): Flow<List<Quote>>
    suspend fun addQuote(bookId: String, text: String, pageNumber: Int?): AppResult<Unit>
    suspend fun deleteQuote(quoteId: String): AppResult<Unit>
}
