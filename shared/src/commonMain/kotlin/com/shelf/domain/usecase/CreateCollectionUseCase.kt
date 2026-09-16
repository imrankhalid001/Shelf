package com.shelf.domain.usecase

import com.shelf.core.result.AppResult
import com.shelf.domain.repository.CollectionRepository

class CreateCollectionUseCase(
    private val collectionRepository: CollectionRepository
) {
    suspend operator fun invoke(name: String, description: String? = null): AppResult<Unit> {
        return collectionRepository.createCollection(name, description)
    }
}
