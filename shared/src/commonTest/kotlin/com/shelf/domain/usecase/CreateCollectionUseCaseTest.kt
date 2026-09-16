package com.shelf.domain.usecase

import com.shelf.core.dispatcher.TestDispatcherProvider
import com.shelf.core.error.AppError
import com.shelf.core.result.AppResult
import com.shelf.data.local.dao.CollectionDao
import com.shelf.data.local.entity.CollectionBookCrossRef
import com.shelf.data.local.entity.CollectionEntity
import com.shelf.data.repository.CollectionRepositoryImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CreateCollectionUseCaseTest {

    private lateinit var createCollectionUseCase: CreateCollectionUseCase
    private lateinit var getCollectionsUseCase: GetCollectionsUseCase

    @BeforeTest
    fun setup() {
        val fakeCollectionDao = object : CollectionDao {
            private val collections = MutableStateFlow<List<CollectionEntity>>(emptyList())

            override suspend fun insertCollection(collection: CollectionEntity) {
                collections.value = collections.value + collection
            }

            override fun observeAllCollections(): Flow<List<CollectionEntity>> {
                return collections
            }

            override suspend fun addBookToCollection(crossRef: CollectionBookCrossRef) {}
            override suspend fun removeBookFromCollection(collectionId: String, bookId: String) {}
            override suspend fun deleteCollection(id: String) {
                collections.value = collections.value.filter { it.id != id }
            }
        }

        val repository = CollectionRepositoryImpl(
            collectionDao = fakeCollectionDao,
            dispatchers = TestDispatcherProvider()
        )
        createCollectionUseCase = CreateCollectionUseCase(repository)
        getCollectionsUseCase = GetCollectionsUseCase(repository)
    }

    @Test
    fun emptyName_returnsValidationError() = runTest {
        val result = createCollectionUseCase(name = "   ", description = "Test")
        assertTrue(result is AppResult.Error)
        assertTrue(result.error is AppError.Validation.EmptyQuery)
    }

    @Test
    fun validName_createsCollectionAndEmits() = runTest {
        val createResult = createCollectionUseCase(name = "Productivity", description = "Self-help books")
        assertTrue(createResult is AppResult.Success)

        val collections = getCollectionsUseCase().first()
        assertEquals(1, collections.size)
        assertEquals("Productivity", collections.first().name)
        assertEquals("Self-help books", collections.first().description)
    }
}
