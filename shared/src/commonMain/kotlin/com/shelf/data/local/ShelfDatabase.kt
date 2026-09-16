package com.shelf.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.shelf.data.local.dao.AuthorDao
import com.shelf.data.local.dao.BookDao
import com.shelf.data.local.dao.CollectionDao
import com.shelf.data.local.dao.NoteQuoteDao
import com.shelf.data.local.dao.ReadingGoalDao
import com.shelf.data.local.dao.ReadingProgressDao
import com.shelf.data.local.dao.ReadingSessionDao
import com.shelf.data.local.entity.AuthorEntity
import com.shelf.data.local.entity.BookAuthorCrossRef
import com.shelf.data.local.entity.BookEntity
import com.shelf.data.local.entity.CollectionBookCrossRef
import com.shelf.data.local.entity.CollectionEntity
import com.shelf.data.local.entity.FavoriteEntity
import com.shelf.data.local.entity.NoteEntity
import com.shelf.data.local.entity.QuoteEntity
import com.shelf.data.local.entity.ReadingGoalEntity
import com.shelf.data.local.entity.ReadingProgressEntity
import com.shelf.data.local.entity.ReadingSessionEntity
import com.shelf.data.local.entity.RecentlyViewedEntity
import com.shelf.data.local.entity.SearchHistoryEntity

@Database(
    entities = [
        BookEntity::class,
        AuthorEntity::class,
        BookAuthorCrossRef::class,
        CollectionEntity::class,
        CollectionBookCrossRef::class,
        ReadingProgressEntity::class,
        ReadingSessionEntity::class,
        NoteEntity::class,
        QuoteEntity::class,
        FavoriteEntity::class,
        SearchHistoryEntity::class,
        ReadingGoalEntity::class,
        RecentlyViewedEntity::class
    ],
    version = 1,
    exportSchema = true
)
@ConstructedBy(ShelfDatabaseConstructor::class)
abstract class ShelfDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun authorDao(): AuthorDao
    abstract fun readingProgressDao(): ReadingProgressDao
    abstract fun collectionDao(): CollectionDao
    abstract fun readingSessionDao(): ReadingSessionDao
    abstract fun noteQuoteDao(): NoteQuoteDao
    abstract fun readingGoalDao(): ReadingGoalDao
}

// Room KMP Expect Constructor
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object ShelfDatabaseConstructor : RoomDatabaseConstructor<ShelfDatabase> {
    override fun initialize(): ShelfDatabase
}

const val SHELF_DATABASE_NAME = "shelf_database.db"
