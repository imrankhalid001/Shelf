package com.shelf.data.local

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

actual fun createTestDatabase(): ShelfDatabase {
    return Room.inMemoryDatabaseBuilder<ShelfDatabase>(
        factory = { ShelfDatabaseConstructor.initialize() }
    ).setDriver(BundledSQLiteDriver()).build()
}
