package com.shelf.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import platform.Foundation.NSHomeDirectory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

fun getDatabaseBuilder(): RoomDatabase.Builder<ShelfDatabase> {
    val dbFilePath = NSHomeDirectory() + "/$SHELF_DATABASE_NAME"
    return Room.databaseBuilder<ShelfDatabase>(
        name = dbFilePath,
        factory = { ShelfDatabaseConstructor.initialize() }
    )
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
}
