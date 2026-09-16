package com.shelf.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider

actual fun createTestDatabase(): ShelfDatabase {
    val context = ApplicationProvider.getApplicationContext<Context>()
    return Room.inMemoryDatabaseBuilder(
        context = context,
        klass = ShelfDatabase::class.java
    ).allowMainThreadQueries().build()
}
