package com.shelf.domain.repository

import com.shelf.core.result.AppResult
import com.shelf.domain.model.ReadingGoal
import com.shelf.domain.model.ReadingSession
import com.shelf.domain.model.ReadingStatistics
import kotlinx.coroutines.flow.Flow

interface StatsRepository {
    fun observeStatistics(): Flow<ReadingStatistics>
    fun observeGoal(year: Int): Flow<ReadingGoal?>
    suspend fun setGoal(year: Int, targetBooks: Int): AppResult<Unit>
    suspend fun logReadingSession(bookId: String, durationSeconds: Long, pagesRead: Int): AppResult<Unit>
    fun observeSessions(): Flow<List<ReadingSession>>
}
