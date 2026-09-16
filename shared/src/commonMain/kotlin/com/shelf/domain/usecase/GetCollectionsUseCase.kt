package com.shelf.domain.usecase

import com.shelf.domain.model.Collection
import com.shelf.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow

class GetCollectionsUseCase(
    private val collectionRepository: CollectionRepository
) {
    operator fun invoke(): Flow<List<Collection>> {
        return collectionRepository.observeCollections()
    }
}
