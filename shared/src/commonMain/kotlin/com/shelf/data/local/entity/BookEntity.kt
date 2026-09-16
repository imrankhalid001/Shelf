package com.shelf.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "books",
    indices = [
        Index(value = ["work_id"]),
        Index(value = ["title"])
    ]
)
data class BookEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "work_id")
    val workId: String,
    val title: String,
    val subtitle: String? = null,
    val description: String? = null,
    @ColumnInfo(name = "cover_id")
    val coverId: Long? = null,
    @ColumnInfo(name = "cover_url")
    val coverUrl: String? = null,
    @ColumnInfo(name = "first_publish_year")
    val firstPublishYear: Int? = null,
    @ColumnInfo(name = "page_count")
    val pageCount: Int = 0,
    val isbn10: String? = null,
    val isbn13: String? = null,
    val subjects: String = "",
    val publisher: String? = null,
    val language: String = "en",
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
)
