package com.shelf.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shelf.data.local.entity.ReadingSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadingSessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: ReadingSessionEntity)

    @Query("SELECT * FROM reading_sessions WHERE book_id = :bookId ORDER BY started_at DESC")
    fun observeSessionsForBook(bookId: String): Flow<List<ReadingSessionEntity>>

    @Query("SELECT * FROM reading_sessions ORDER BY started_at DESC")
    fun observeAllSessions(): Flow<List<ReadingSessionEntity>>
}
