package com.shelf.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "quotes",
    foreignKeys = [
        ForeignKey(
            entity = BookEntity::class,
            parentColumns = ["id"],
            childColumns = ["book_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["book_id"])]
)
data class QuoteEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "book_id")
    val bookId: String,
    val text: String,
    @ColumnInfo(name = "page_number")
    val pageNumber: Int? = null,
    @ColumnInfo(name = "created_at")
    val createdAt: Long
)
