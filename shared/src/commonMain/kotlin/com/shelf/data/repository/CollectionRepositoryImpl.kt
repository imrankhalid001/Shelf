package com.shelf.data.repository

import com.shelf.core.dispatcher.DispatcherProvider
import com.shelf.core.error.AppError
import com.shelf.core.result.AppResult
import com.shelf.core.utils.DateUtils
import com.shelf.data.local.dao.CollectionDao
import com.shelf.data.local.entity.CollectionBookCrossRef
import com.shelf.data.local.entity.CollectionEntity
import com.shelf.domain.model.Collection
import com.shelf.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class CollectionRepositoryImpl(
    private val collectionDao: CollectionDao,
    private val dispatchers: DispatcherProvider
) : CollectionRepository {

    override fun observeCollections(): Flow<List<Collection>> {
        return collectionDao.observeAllCollections().map { list ->
            list.map { entity ->
                Collection(
                    id = entity.id,
                    name = entity.name,
                    description = entity.description,
                    coverBookId = entity.coverBookId,
                    createdAt = entity.createdAt,
                    updatedAt = entity.updatedAt
                )
            }
        }.flowOn(dispatchers.io)
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun createCollection(
        name: String,
        description: String?
    ): AppResult<Unit> = withContext(dispatchers.io) {
        try {
            if (name.isBlank()) {
                return@withContext AppResult.Error(AppError.Validation.EmptyQuery)
            }
            val now = DateUtils.nowEpochMillis()
            val entity = CollectionEntity(
                id = Uuid.random().toString(),
                name = name.trim(),
                description = description?.trim(),
                createdAt = now,
                updatedAt = now
            )
            collectionDao.insertCollection(entity)
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error(AppError.Database.WriteFailed(e))
        }
    }

    override suspend fun deleteCollection(id: String): AppResult<Unit> = withContext(dispatchers.io) {
        try {
            collectionDao.deleteCollection(id)
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error(AppError.Database.WriteFailed(e))
        }
    }

    override suspend fun addBookToCollection(
        collectionId: String,
        bookId: String
    ): AppResult<Unit> = withContext(dispatchers.io) {
        try {
            val now = DateUtils.nowEpochMillis()
            val crossRef = CollectionBookCrossRef(
                collectionId = collectionId,
                bookId = bookId,
                addedAt = now
            )
            collectionDao.addBookToCollection(crossRef)
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error(AppError.Database.WriteFailed(e))
        }
    }

    override suspend fun removeBookFromCollection(
        collectionId: String,
        bookId: String
    ): AppResult<Unit> = withContext(dispatchers.io) {
        try {
            collectionDao.removeBookFromCollection(collectionId, bookId)
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error(AppError.Database.WriteFailed(e))
        }
    }
}
