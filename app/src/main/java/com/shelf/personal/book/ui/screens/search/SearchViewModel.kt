package com.shelf.personal.book.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shelf.core.result.AppResult
import com.shelf.domain.model.Book
import com.shelf.domain.usecase.SearchBooksUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

data class SearchUiState(
    val query: String = "",
    val searchResult: AppResult<List<Book>> = AppResult.Success(emptyList())
)

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class SearchViewModel(
    private val searchBooksUseCase: SearchBooksUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _query
                .debounce(400)
                .distinctUntilChanged()
                .flatMapLatest { searchTerms ->
                    if (searchTerms.isBlank()) {
                        flowOf(AppResult.Success(emptyList()))
                    } else {
                        searchBooksUseCase(searchTerms)
                    }
                }
                .collect { result ->
                    _uiState.value = _uiState.value.copy(searchResult = result)
                }
        }
    }

    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
        _uiState.value = _uiState.value.copy(query = newQuery)
    }
}
