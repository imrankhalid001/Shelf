package com.shelf.data.repository

import com.shelf.core.dispatcher.DispatcherProvider
import com.shelf.core.error.AppError
import com.shelf.core.result.AppResult
import com.shelf.core.utils.DateUtils
import com.shelf.data.local.dao.ReadingGoalDao
import com.shelf.data.local.dao.ReadingProgressDao
import com.shelf.data.local.dao.ReadingSessionDao
import com.shelf.data.local.entity.ReadingGoalEntity
import com.shelf.data.local.entity.ReadingSessionEntity
import com.shelf.data.mapper.toDomain
import com.shelf.domain.model.ReadingGoal
import com.shelf.domain.model.ReadingSession
import com.shelf.domain.model.ReadingStatistics
import com.shelf.domain.repository.StatsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class StatsRepositoryImpl(
    private val readingGoalDao: ReadingGoalDao,
    private val readingSessionDao: ReadingSessionDao,
    private val readingProgressDao: ReadingProgressDao,
    private val dispatchers: DispatcherProvider
) : StatsRepository {

    override fun observeStatistics(): Flow<ReadingStatistics> {
        return combine(
            readingProgressDao.observeAllProgress(),
            readingSessionDao.observeAllSessions()
        ) { progressList, sessions ->
            val finishedCount = progressList.count { it.status == "FINISHED" }
            val totalPagesFromSessions = sessions.sumOf { it.pagesRead }
            val totalPagesFromProgress = progressList.sumOf { it.currentPage }
            val totalPages = maxOf(totalPagesFromSessions, totalPagesFromProgress)

            val totalDurationFromSessions = sessions.sumOf { it.durationSeconds } / 60
            val estimatedMinutes = (totalPages * 90L) / 60
            val totalReadingTimeMinutes = maxOf(totalDurationFromSessions, estimatedMinutes)

            val progressDates = progressList.map { DateUtils.epochMillisToLocalDate(it.updatedAt) }
            val sessionDates = sessions.map { DateUtils.epochMillisToLocalDate(it.startedAt) }
            val allActivityDates = (progressDates + sessionDates)
                .distinct()
                .sortedDescending()

            var currentStreak = 0
            val today = DateUtils.epochMillisToLocalDate(DateUtils.nowEpochMillis())
            var checkDate = today

            for (date in allActivityDates) {
                if (date == checkDate) {
                    currentStreak++
                    checkDate = LocalDate.fromEpochDays(checkDate.toEpochDays() - 1)
                } else if (date == LocalDate.fromEpochDays(today.toEpochDays() - 1) && currentStreak == 0) {
                    currentStreak++
                    checkDate = LocalDate.fromEpochDays(date.toEpochDays() - 1)
                } else {
                    break
                }
            }

            ReadingStatistics(
                totalBooksReadThisYear = finishedCount,
                totalPagesRead = totalPages,
                totalReadingTimeMinutes = totalReadingTimeMinutes,
                currentStreakDays = if (totalPages > 0 && currentStreak == 0) 1 else currentStreak,
                longestStreakDays = maxOf(1, currentStreak),
                averageBooksPerMonth = if (finishedCount > 0) finishedCount.toDouble() / 12.0 else 0.0
            )
        }.flowOn(dispatchers.io)
    }

    override fun observeGoal(year: Int): Flow<ReadingGoal?> {
        return readingGoalDao.observeGoalForYear(year).map { entity ->
            entity?.toDomain()
        }.flowOn(dispatchers.io)
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun setGoal(year: Int, targetBooks: Int): AppResult<Unit> = withContext(dispatchers.io) {
        try {
            val now = DateUtils.nowEpochMillis()
            val goalEntity = ReadingGoalEntity(
                id = Uuid.random().toString(),
                year = year,
                targetBooks = targetBooks,
                completedBooks = 0,
                createdAt = now,
                updatedAt = now
            )
            readingGoalDao.upsertGoal(goalEntity)
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error(AppError.Database.WriteFailed(e))
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun logReadingSession(
        bookId: String,
        durationSeconds: Long,
        pagesRead: Int
    ): AppResult<Unit> = withContext(dispatchers.io) {
        try {
            val now = DateUtils.nowEpochMillis()
            val sessionEntity = ReadingSessionEntity(
                id = Uuid.random().toString(),
                bookId = bookId,
                startedAt = now - (durationSeconds * 1000),
                endedAt = now,
                durationSeconds = durationSeconds,
                pagesRead = pagesRead
            )
            readingSessionDao.insertSession(sessionEntity)
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error(AppError.Database.WriteFailed(e))
        }
    }

    override fun observeSessions(): Flow<List<ReadingSession>> {
        return readingSessionDao.observeAllSessions().map { list ->
            list.map { it.toDomain() }
        }.flowOn(dispatchers.io)
    }
}
