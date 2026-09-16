package com.shelf.personal.book.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shelf.domain.model.Book
import com.shelf.domain.model.ReadingStatistics
import com.shelf.domain.usecase.GetLibraryUseCase
import com.shelf.domain.usecase.GetReadingStatisticsUseCase
import com.shelf.domain.usecase.GetRecommendationsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class HomeUiState(
    val currentlyReadingBook: Book? = null,
    val recommendedBooks: List<Book> = emptyList(),
    val statistics: ReadingStatistics = ReadingStatistics(),
    val isLoading: Boolean = true
)

class HomeViewModel(
    getLibraryUseCase: GetLibraryUseCase,
    getReadingStatisticsUseCase: GetReadingStatisticsUseCase,
    getRecommendationsUseCase: GetRecommendationsUseCase
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = combine(
        getLibraryUseCase.observeAll(),
        getReadingStatisticsUseCase(),
        getRecommendationsUseCase()
    ) { library, stats, recommendations ->
        val currentlyReading = library.firstOrNull { it.readingProgress?.status?.name == "READING" }
        HomeUiState(
            currentlyReadingBook = currentlyReading,
            recommendedBooks = recommendations,
            statistics = stats,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState(isLoading = true)
    )
}
