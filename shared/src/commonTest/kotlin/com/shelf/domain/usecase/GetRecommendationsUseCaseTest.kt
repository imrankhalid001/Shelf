package com.shelf.domain.usecase

import com.shelf.core.dispatcher.TestDispatcherProvider
import com.shelf.core.error.AppError
import com.shelf.core.result.AppResult
import com.shelf.data.local.entity.BookEntity
import com.shelf.data.local.fakes.FakeBookDao
import com.shelf.data.local.fakes.FakeNoteQuoteDao
import com.shelf.data.local.fakes.FakeReadingProgressDao
import com.shelf.data.remote.api.OpenLibraryApi
import com.shelf.data.remote.dto.SearchResponseDto
import com.shelf.data.repository.BookRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class GetRecommendationsUseCaseTest {

    @Test
    fun emptyLibrary_returnsEmptyRecommendations() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        val fakeBookDao = FakeBookDao()
        val fakeProgressDao = FakeReadingProgressDao()
        val fakeNoteQuoteDao = FakeNoteQuoteDao()
        val fakeApi = object : OpenLibraryApi {
            override suspend fun searchBooks(query: String, page: Int, limit: Int) = AppResult.Success(SearchResponseDto())
            override suspend fun getWorkDetails(workId: String) = AppResult.Error(AppError.Network.NoInternet)
            override suspend fun getAuthorDetails(authorId: String) = AppResult.Error(AppError.Network.NoInternet)
        }

        val repository = BookRepositoryImpl(fakeBookDao, fakeProgressDao, fakeNoteQuoteDao, fakeApi, TestDispatcherProvider(testDispatcher))
        val useCase = GetRecommendationsUseCase(repository)

        val recommendations = useCase().first()
        assertTrue(recommendations.isEmpty())
    }

    @Test
    fun matchingSubjects_returnsRecommendedBooks() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        val fakeBookDao = FakeBookDao()
        val fakeProgressDao = FakeReadingProgressDao()
        val fakeNoteQuoteDao = FakeNoteQuoteDao()
        val fakeApi = object : OpenLibraryApi {
            override suspend fun searchBooks(query: String, page: Int, limit: Int) = AppResult.Success(SearchResponseDto())
            override suspend fun getWorkDetails(workId: String) = AppResult.Error(AppError.Network.NoInternet)
            override suspend fun getAuthorDetails(authorId: String) = AppResult.Error(AppError.Network.NoInternet)
        }

        val repository = BookRepositoryImpl(fakeBookDao, fakeProgressDao, fakeNoteQuoteDao, fakeApi, TestDispatcherProvider(testDispatcher))
        val useCase = GetRecommendationsUseCase(repository)

        val book1 = BookEntity(id = "1", workId = "1", title = "Book 1", subjects = "Psychology, Self-Help", createdAt = 1000L, updatedAt = 1000L)
        val book2 = BookEntity(id = "2", workId = "2", title = "Book 2", subjects = "Psychology, Science", createdAt = 2000L, updatedAt = 2000L)
        fakeBookDao.insertOrUpdate(book1)
        fakeBookDao.insertOrUpdate(book2)

        val recommendations = useCase().first()
        assertEquals(2, recommendations.size)
    }
}
