package com.shelf.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "search_history",
    indices = [Index(value = ["searched_at"])]
)
data class SearchHistoryEntity(
    @PrimaryKey
    val id: String,
    val query: String,
    @ColumnInfo(name = "searched_at")
    val searchedAt: Long
)
