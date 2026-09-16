package com.shelf.domain.repository

import com.shelf.core.result.AppResult
import com.shelf.domain.model.Collection
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {
    fun observeCollections(): Flow<List<Collection>>
    suspend fun createCollection(name: String, description: String? = null): AppResult<Unit>
    suspend fun deleteCollection(id: String): AppResult<Unit>
    suspend fun addBookToCollection(collectionId: String, bookId: String): AppResult<Unit>
    suspend fun removeBookFromCollection(collectionId: String, bookId: String): AppResult<Unit>
}
