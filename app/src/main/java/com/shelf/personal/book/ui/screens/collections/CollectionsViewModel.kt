package com.shelf.personal.book.ui.screens.collections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shelf.domain.model.Collection
import com.shelf.domain.repository.CollectionRepository
import com.shelf.domain.usecase.CreateCollectionUseCase
import com.shelf.domain.usecase.GetCollectionsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CollectionsUiState(
    val collections: List<Collection> = emptyList(),
    val isLoading: Boolean = true
)

class CollectionsViewModel(
    getCollectionsUseCase: GetCollectionsUseCase,
    private val createCollectionUseCase: CreateCollectionUseCase,
    private val collectionRepository: CollectionRepository
) : ViewModel() {

    val uiState: StateFlow<CollectionsUiState> = getCollectionsUseCase().map { list ->
        CollectionsUiState(
            collections = list,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CollectionsUiState(isLoading = true)
    )

    fun createCollection(name: String, description: String?) {
        viewModelScope.launch {
            createCollectionUseCase(name, description)
        }
    }

    fun deleteCollection(id: String) {
        viewModelScope.launch {
            collectionRepository.deleteCollection(id)
        }
    }
}
