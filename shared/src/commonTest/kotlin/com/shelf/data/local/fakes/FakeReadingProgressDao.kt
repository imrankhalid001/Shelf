package com.shelf.data.local.fakes

import com.shelf.data.local.dao.ReadingProgressDao
import com.shelf.data.local.entity.ReadingProgressEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeReadingProgressDao : ReadingProgressDao {
    private val progressMap = MutableStateFlow<Map<String, ReadingProgressEntity>>(emptyMap())

    override suspend fun upsertProgress(progress: ReadingProgressEntity) {
        progressMap.value = progressMap.value + (progress.bookId to progress)
    }

    override suspend fun getProgressForBook(bookId: String): ReadingProgressEntity? {
        return progressMap.value[bookId]
    }

    override fun observeProgressForBook(bookId: String): Flow<ReadingProgressEntity?> {
        return progressMap.map { it[bookId] }
    }

    override fun observeProgressByStatus(status: String): Flow<List<ReadingProgressEntity>> {
        return progressMap.map { map ->
            map.values.filter { it.status == status }.sortedByDescending { it.updatedAt }
        }
    }

    override fun observeAllProgress(): Flow<List<ReadingProgressEntity>> {
        return progressMap.map { it.values.sortedByDescending { p -> p.updatedAt } }
    }
}
