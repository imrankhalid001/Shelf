package com.shelf.personal.book.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shelf.core.error.AppError
import com.shelf.core.result.AppResult
import com.shelf.domain.model.Book
import com.shelf.domain.model.Note
import com.shelf.domain.model.Quote
import com.shelf.domain.model.ReadingStatus
import com.shelf.domain.repository.BookRepository
import com.shelf.domain.usecase.GetBookDetailsUseCase
import com.shelf.domain.usecase.UpdateReadingProgressUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class BookDetailsUiState(
    val bookResult: AppResult<Book> = AppResult.Loading,
    val notes: List<Note> = emptyList(),
    val quotes: List<Quote> = emptyList()
)

@OptIn(ExperimentalCoroutinesApi::class)
class BookDetailsViewModel(
    private val bookRepository: BookRepository,
    private val getBookDetailsUseCase: GetBookDetailsUseCase,
    private val updateReadingProgressUseCase: UpdateReadingProgressUseCase
) : ViewModel() {

    private val _bookId = MutableStateFlow<String?>(null)

    val uiState: StateFlow<BookDetailsUiState> = _bookId.flatMapLatest { id ->
        if (id == null) {
            flowOf(BookDetailsUiState(bookResult = AppResult.Error(AppError.Database.NotFound)))
        } else {
            getBookDetailsUseCase(id).map { result ->
                BookDetailsUiState(
                    bookResult = result,
                    notes = emptyList(),
                    quotes = emptyList()
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BookDetailsUiState(bookResult = AppResult.Loading)
    )

    fun loadBook(bookId: String) {
        _bookId.value = bookId
    }

    fun updateProgress(bookId: String, currentPage: Int, totalPages: Int) {
        viewModelScope.launch {
            updateReadingProgressUseCase(bookId, currentPage, totalPages)
        }
    }

    fun updateStatus(bookId: String, status: ReadingStatus) {
        viewModelScope.launch {
            bookRepository.updateReadingStatus(bookId, status)
        }
    }

    fun saveBook(book: Book) {
        viewModelScope.launch {
            bookRepository.saveBook(book)
        }
    }

    fun addNote(bookId: String, content: String, pageNumber: Int?) {
        viewModelScope.launch {
            bookRepository.addNote(bookId, content, pageNumber)
        }
    }

    fun addQuote(bookId: String, text: String, pageNumber: Int?) {
        viewModelScope.launch {
            bookRepository.addQuote(bookId, text, pageNumber)
        }
    }
}
