package com.shelf.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "reading_goals",
    indices = [Index(value = ["year"], unique = true)]
)
data class ReadingGoalEntity(
    @PrimaryKey
    val id: String,
    val year: Int,
    @ColumnInfo(name = "target_books")
    val targetBooks: Int,
    @ColumnInfo(name = "completed_books")
    val completedBooks: Int = 0,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
)
