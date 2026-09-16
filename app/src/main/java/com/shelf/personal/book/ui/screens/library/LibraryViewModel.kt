package com.shelf.personal.book.ui.screens.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shelf.domain.model.Book
import com.shelf.domain.model.ReadingStatus
import com.shelf.domain.usecase.GetLibraryUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class LibraryUiState(
    val selectedStatus: ReadingStatus? = null,
    val books: List<Book> = emptyList(),
    val isLoading: Boolean = true
)

@OptIn(ExperimentalCoroutinesApi::class)
class LibraryViewModel(
    private val getLibraryUseCase: GetLibraryUseCase
) : ViewModel() {

    private val _selectedStatus = MutableStateFlow<ReadingStatus?>(null)
    val selectedStatus: StateFlow<ReadingStatus?> = _selectedStatus.asStateFlow()

    val uiState: StateFlow<LibraryUiState> = _selectedStatus.flatMapLatest { status ->
        if (status == null) {
            getLibraryUseCase.observeAll()
        } else {
            getLibraryUseCase.observeByStatus(status)
        }
    }.map { list ->
        LibraryUiState(
            selectedStatus = _selectedStatus.value,
            books = list,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LibraryUiState(isLoading = true)
    )

    fun onStatusSelected(status: ReadingStatus?) {
        _selectedStatus.value = status
    }
}
