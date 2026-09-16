package com.shelf.data.local.fakes

import com.shelf.data.local.dao.BookDao
import com.shelf.data.local.entity.BookEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeBookDao : BookDao {
    private val booksMap = MutableStateFlow<Map<String, BookEntity>>(emptyMap())

    override suspend fun insertOrUpdate(book: BookEntity) {
        booksMap.value = booksMap.value + (book.id to book)
    }

    override suspend fun insertAll(books: List<BookEntity>) {
        val updated = booksMap.value.toMutableMap()
        books.forEach { updated[it.id] = it }
        booksMap.value = updated
    }

    override suspend fun getBookById(id: String): BookEntity? {
        return booksMap.value[id]
    }

    override fun observeBookById(id: String): Flow<BookEntity?> {
        return booksMap.map { it[id] }
    }

    override fun observeAllBooks(): Flow<List<BookEntity>> {
        return booksMap.map { it.values.sortedByDescending { b -> b.createdAt } }
    }

    override fun searchBooks(query: String): Flow<List<BookEntity>> {
        return booksMap.map { map ->
            map.values.filter {
                it.title.contains(query, ignoreCase = true) || it.subjects.contains(query, ignoreCase = true)
            }
        }
    }

    override suspend fun deleteBookById(id: String) {
        booksMap.value = booksMap.value - id
    }
}
