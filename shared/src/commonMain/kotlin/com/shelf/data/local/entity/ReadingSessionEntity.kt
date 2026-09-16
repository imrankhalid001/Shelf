package com.shelf.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "reading_sessions",
    foreignKeys = [
        ForeignKey(
            entity = BookEntity::class,
            parentColumns = ["id"],
            childColumns = ["book_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["book_id"]),
        Index(value = ["started_at"])
    ]
)
data class ReadingSessionEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "book_id")
    val bookId: String,
    @ColumnInfo(name = "started_at")
    val startedAt: Long,
    @ColumnInfo(name = "ended_at")
    val endedAt: Long,
    @ColumnInfo(name = "duration_seconds")
    val durationSeconds: Long,
    @ColumnInfo(name = "pages_read")
    val pagesRead: Int
)
