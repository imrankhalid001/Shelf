package com.shelf.domain.usecase

import com.shelf.core.dispatcher.TestDispatcherProvider
import com.shelf.core.error.AppError
import com.shelf.core.result.AppResult
import com.shelf.data.local.dao.ReadingGoalDao
import com.shelf.data.local.dao.ReadingSessionDao
import com.shelf.data.local.entity.ReadingGoalEntity
import com.shelf.data.local.entity.ReadingSessionEntity
import com.shelf.data.local.fakes.FakeBookDao
import com.shelf.data.local.fakes.FakeNoteQuoteDao
import com.shelf.data.local.fakes.FakeReadingProgressDao
import com.shelf.data.remote.api.OpenLibraryApi
import com.shelf.data.remote.dto.SearchResponseDto
import com.shelf.data.repository.BookRepositoryImpl
import com.shelf.data.repository.StatsRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class LogReadingSessionUseCaseTest {

    private lateinit var logReadingSessionUseCase: LogReadingSessionUseCase

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

        val fakeGoalDao = object : ReadingGoalDao {
            private val goals = MutableStateFlow<Map<Int, ReadingGoalEntity>>(emptyMap())
            override suspend fun upsertGoal(goal: ReadingGoalEntity) { goals.value = goals.value + (goal.year to goal) }
            override fun observeGoalForYear(year: Int) = MutableStateFlow<ReadingGoalEntity?>(null)
        }

        val fakeSessionDao = object : ReadingSessionDao {
            private val sessions = MutableStateFlow<List<ReadingSessionEntity>>(emptyList())
            override suspend fun insertSession(session: ReadingSessionEntity) { sessions.value = sessions.value + session }
            override fun observeSessionsForBook(bookId: String) = MutableStateFlow<List<ReadingSessionEntity>>(emptyList())
            override fun observeAllSessions() = sessions
        }

        val dispatchers = TestDispatcherProvider()
        val bookRepository = BookRepositoryImpl(fakeBookDao, fakeProgressDao, fakeNoteQuoteDao, fakeApi, dispatchers)
        val statsRepository = StatsRepositoryImpl(fakeGoalDao, fakeSessionDao, fakeProgressDao, dispatchers)

        logReadingSessionUseCase = LogReadingSessionUseCase(statsRepository, bookRepository)
    }

    @Test
    fun zeroDuration_returnsValidationError() = runTest {
        val result = logReadingSessionUseCase(
            bookId = "OL123",
            durationSeconds = 0,
            startPage = 10,
            endPage = 20,
            totalPages = 100
        )
        assertTrue(result is AppResult.Error)
        assertTrue(result.error is AppError.Validation.Custom)
    }

    @Test
    fun endPageLessThanStartPage_returnsValidationError() = runTest {
        val result = logReadingSessionUseCase(
            bookId = "OL123",
            durationSeconds = 600,
            startPage = 50,
            endPage = 20,
            totalPages = 100
        )
        assertTrue(result is AppResult.Error)
        assertTrue(result.error is AppError.Validation.Custom)
    }

    @Test
    fun validSession_logsSuccessfully() = runTest {
        val result = logReadingSessionUseCase(
            bookId = "OL123",
            durationSeconds = 1200,
            startPage = 10,
            endPage = 30,
            totalPages = 200
        )
        assertTrue(result is AppResult.Success)
    }
}
