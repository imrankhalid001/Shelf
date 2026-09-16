package com.shelf.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "reading_progress",
    foreignKeys = [
        ForeignKey(
            entity = BookEntity::class,
            parentColumns = ["id"],
            childColumns = ["book_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["status"])]
)
data class ReadingProgressEntity(
    @PrimaryKey
    @ColumnInfo(name = "book_id")
    val bookId: String,
    @ColumnInfo(name = "current_page")
    val currentPage: Int = 0,
    @ColumnInfo(name = "total_pages")
    val totalPages: Int = 0,
    val percentage: Double = 0.0,
    val status: String,
    @ColumnInfo(name = "started_at")
    val startedAt: Long? = null,
    @ColumnInfo(name = "finished_at")
    val finishedAt: Long? = null,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
)
