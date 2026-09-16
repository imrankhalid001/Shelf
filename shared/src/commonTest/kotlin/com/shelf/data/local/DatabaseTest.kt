package com.shelf.data.local

import com.shelf.data.local.entity.BookEntity
import com.shelf.data.local.entity.ReadingProgressEntity
import com.shelf.data.local.fakes.FakeBookDao
import com.shelf.data.local.fakes.FakeReadingProgressDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DatabaseTest {

    private lateinit var bookDao: FakeBookDao
    private lateinit var readingProgressDao: FakeReadingProgressDao

    @BeforeTest
    fun setup() {
        bookDao = FakeBookDao()
        readingProgressDao = FakeReadingProgressDao()
    }

    @Test
    fun insertAndGetBook() = runTest {
        val book = BookEntity(
            id = "OL27517W",
            workId = "OL27517W",
            title = "Atomic Habits",
            subtitle = "An Easy & Proven Way to Build Good Habits",
            pageCount = 320,
            createdAt = 1000L,
            updatedAt = 1000L
        )

        bookDao.insertOrUpdate(book)

        val retrieved = bookDao.getBookById("OL27517W")
        assertNotNull(retrieved)
        assertEquals("Atomic Habits", retrieved.title)
        assertEquals(320, retrieved.pageCount)
    }

    @Test
    fun insertAndObserveReadingProgress() = runTest {
        val book = BookEntity(
            id = "OL27517W",
            workId = "OL27517W",
            title = "Atomic Habits",
            createdAt = 1000L,
            updatedAt = 1000L
        )
        bookDao.insertOrUpdate(book)

        val progress = ReadingProgressEntity(
            bookId = "OL27517W",
            currentPage = 180,
            totalPages = 320,
            percentage = 56.25,
            status = "READING",
            updatedAt = 1000L
        )
        readingProgressDao.upsertProgress(progress)

        val retrievedProgress = readingProgressDao.observeProgressForBook("OL27517W").first()
        assertNotNull(retrievedProgress)
        assertEquals(180, retrievedProgress.currentPage)
        assertEquals("READING", retrievedProgress.status)
    }
}
