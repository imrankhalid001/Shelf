package com.shelf.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shelf.data.local.entity.ReadingProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadingProgressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProgress(progress: ReadingProgressEntity)

    @Query("SELECT * FROM reading_progress WHERE book_id = :bookId")
    suspend fun getProgressForBook(bookId: String): ReadingProgressEntity?

    @Query("SELECT * FROM reading_progress WHERE book_id = :bookId")
    fun observeProgressForBook(bookId: String): Flow<ReadingProgressEntity?>

    @Query("SELECT * FROM reading_progress WHERE status = :status ORDER BY updated_at DESC")
    fun observeProgressByStatus(status: String): Flow<List<ReadingProgressEntity>>

    @Query("SELECT * FROM reading_progress ORDER BY updated_at DESC")
    fun observeAllProgress(): Flow<List<ReadingProgressEntity>>
}
