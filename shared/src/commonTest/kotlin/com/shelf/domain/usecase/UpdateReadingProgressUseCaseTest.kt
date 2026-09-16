package com.shelf.domain.usecase

import com.shelf.core.dispatcher.TestDispatcherProvider
import com.shelf.core.error.AppError
import com.shelf.core.result.AppResult
import com.shelf.data.local.fakes.FakeBookDao
import com.shelf.data.local.fakes.FakeNoteQuoteDao
import com.shelf.data.local.fakes.FakeReadingProgressDao
import com.shelf.data.repository.BookRepositoryImpl
import com.shelf.data.remote.api.OpenLibraryApi
import com.shelf.data.remote.dto.AuthorDto
import com.shelf.data.remote.dto.SearchResponseDto
import com.shelf.data.remote.dto.WorkDetailsDto
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class UpdateReadingProgressUseCaseTest {

    private lateinit var useCase: UpdateReadingProgressUseCase

    @BeforeTest
    fun setup() {
        val fakeBookDao = FakeBookDao()
        val fakeProgressDao = FakeReadingProgressDao()
        val fakeNoteQuoteDao = FakeNoteQuoteDao()
        val fakeApi = object : OpenLibraryApi {
            override suspend fun searchBooks(query: String, page: Int, limit: Int) = AppResult.Success(SearchResponseDto())
            override suspend fun getWorkDetails(workId: String) = AppResult.Error(AppError.Network.NoInternet)
            override suspend fun getAuthorDetails(authorId: String) = AppResult.Error(AppError.Network.NoInternet)
        }
        val repository = BookRepositoryImpl(
            bookDao = fakeBookDao,
            readingProgressDao = fakeProgressDao,
            noteQuoteDao = fakeNoteQuoteDao,
            openLibraryApi = fakeApi,
            dispatchers = TestDispatcherProvider()
        )
        useCase = UpdateReadingProgressUseCase(repository)
    }

    @Test
    fun negativePageNumber_returnsValidationError() = runTest {
        val result = useCase(bookId = "OL123", currentPage = -5, totalPages = 100)
        assertTrue(result is AppResult.Error)
        assertTrue(result.error is AppError.Validation.InvalidPageNumber)
    }

    @Test
    fun validPageNumber_updatesProgressSuccessfully() = runTest {
        val result = useCase(bookId = "OL123", currentPage = 50, totalPages = 100)
        assertTrue(result is AppResult.Success)
    }
}
