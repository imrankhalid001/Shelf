package com.shelf.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shelf.data.local.entity.ReadingGoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadingGoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertGoal(goal: ReadingGoalEntity)

    @Query("SELECT * FROM reading_goals WHERE year = :year")
    fun observeGoalForYear(year: Int): Flow<ReadingGoalEntity?>
}
