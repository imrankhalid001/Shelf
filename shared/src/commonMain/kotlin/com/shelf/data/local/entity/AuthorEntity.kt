package com.shelf.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "authors",
    indices = [Index(value = ["name"])]
)
data class AuthorEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val bio: String? = null,
    @ColumnInfo(name = "photo_url")
    val photoUrl: String? = null,
    @ColumnInfo(name = "created_at")
    val createdAt: Long
)
