package com.shelf.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "collection_books",
    primaryKeys = ["collection_id", "book_id"],
    foreignKeys = [
        ForeignKey(
            entity = CollectionEntity::class,
            parentColumns = ["id"],
            childColumns = ["collection_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = BookEntity::class,
            parentColumns = ["id"],
            childColumns = ["book_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["book_id"])]
)
data class CollectionBookCrossRef(
    @ColumnInfo(name = "collection_id")
    val collectionId: String,
    @ColumnInfo(name = "book_id")
    val bookId: String,
    @ColumnInfo(name = "added_at")
    val addedAt: Long
)
