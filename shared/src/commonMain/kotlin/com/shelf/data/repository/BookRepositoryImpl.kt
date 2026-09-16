package com.shelf.data.repository

import com.shelf.core.dispatcher.DispatcherProvider
import com.shelf.core.error.AppError
import com.shelf.core.result.AppResult
import com.shelf.core.utils.DateUtils
import com.shelf.core.utils.StringUtils
import com.shelf.data.local.dao.BookDao
import com.shelf.data.local.dao.NoteQuoteDao
import com.shelf.data.local.dao.ReadingProgressDao
import com.shelf.data.local.entity.BookEntity
import com.shelf.data.local.entity.NoteEntity
import com.shelf.data.local.entity.QuoteEntity
import com.shelf.data.local.entity.ReadingProgressEntity
import com.shelf.data.mapper.toBookEntity
import com.shelf.data.mapper.toDomain
import com.shelf.data.remote.api.OpenLibraryApi
import com.shelf.domain.model.Book
import com.shelf.domain.model.Note
import com.shelf.domain.model.Quote
import com.shelf.domain.model.ReadingStatus
import com.shelf.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class BookRepositoryImpl(
    private val bookDao: BookDao,
    private val readingProgressDao: ReadingProgressDao,
    private val noteQuoteDao: NoteQuoteDao,
    private val openLibraryApi: OpenLibraryApi,
    private val dispatchers: DispatcherProvider
) : BookRepository {

    override fun searchBooks(query: String, page: Int): Flow<AppResult<List<Book>>> = flow {
        emit(AppResult.Loading)
        if (query.isBlank()) {
            emit(AppResult.Success(emptyList()))
            return@flow
        }

        when (val apiResult = openLibraryApi.searchBooks(query, page)) {
            is AppResult.Success -> {
                val books = apiResult.data.docs.map { doc ->
                    val entity = doc.toBookEntity()
                    entity.toDomain()
                }
                emit(AppResult.Success(books))
            }
            is AppResult.Error -> {
                emit(AppResult.Error(apiResult.error))
            }
            is AppResult.Loading -> emit(AppResult.Loading)
        }
    }.flowOn(dispatchers.io)

    override fun observeLibrary(): Flow<List<Book>> {
        return combine(
            bookDao.observeAllBooks(),
            readingProgressDao.observeAllProgress()
        ) { books, progressList ->
            val progressMap = progressList.associateBy { it.bookId }
            books.map { entity ->
                val progressEntity = progressMap[entity.id]
                entity.toDomain(progress = progressEntity?.toDomain())
            }
        }.flowOn(dispatchers.io)
    }

    override fun observeLibraryByStatus(status: ReadingStatus): Flow<List<Book>> {
        return combine(
            bookDao.observeAllBooks(),
            readingProgressDao.observeProgressByStatus(status.name)
        ) { books, progressList ->
            progressList.mapNotNull { progressEntity ->
                val bookEntity = books.find { it.id == progressEntity.bookId }
                bookEntity?.toDomain(progress = progressEntity.toDomain())
            }
        }.flowOn(dispatchers.io)
    }

    override fun observeBookDetails(id: String): Flow<AppResult<Book>> {
        return combine(
            bookDao.observeBookById(id),
            readingProgressDao.observeProgressForBook(id)
        ) { bookEntity, progressEntity ->
            if (bookEntity == null) {
                AppResult.Error(AppError.Database.NotFound)
            } else {
                AppResult.Success(bookEntity.toDomain(progress = progressEntity?.toDomain()))
            }
        }.flowOn(dispatchers.io)
    }

    override suspend fun saveBook(book: Book): AppResult<Unit> = withContext(dispatchers.io) {
        try {
            val now = DateUtils.nowEpochMillis()
            val entity = BookEntity(
                id = book.id,
                workId = book.workId,
                title = book.title,
                subtitle = book.subtitle,
                description = book.description,
                coverId = book.coverId,
                coverUrl = book.coverUrl,
                firstPublishYear = book.firstPublishYear,
                pageCount = book.pageCount,
                isbn10 = book.isbn10,
                isbn13 = book.isbn13,
                subjects = book.subjects.joinToString(", "),
                publisher = book.publisher,
                language = book.language,
                createdAt = if (book.createdAt > 0) book.createdAt else now,
                updatedAt = now
            )
            bookDao.insertOrUpdate(entity)

            val initialStatus = book.readingProgress?.status ?: ReadingStatus.WANT_TO_READ
            val progressEntity = ReadingProgressEntity(
                bookId = book.id,
                currentPage = book.readingProgress?.currentPage ?: 0,
                totalPages = book.pageCount,
                percentage = StringUtils.formatReadingPercentage(book.readingProgress?.currentPage ?: 0, book.pageCount),
                status = initialStatus.name,
                updatedAt = now
            )
            readingProgressDao.upsertProgress(progressEntity)
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error(AppError.Database.WriteFailed(e))
        }
    }

    override suspend fun updateReadingProgress(
        bookId: String,
        currentPage: Int,
        totalPages: Int
    ): AppResult<Unit> = withContext(dispatchers.io) {
        try {
            val now = DateUtils.nowEpochMillis()
            val existing = readingProgressDao.getProgressForBook(bookId)
            val percentage = StringUtils.formatReadingPercentage(currentPage, totalPages)
            val newStatus = if (currentPage >= totalPages && totalPages > 0) {
                ReadingStatus.FINISHED.name
            } else {
                existing?.status ?: ReadingStatus.READING.name
            }

            val updatedEntity = ReadingProgressEntity(
                bookId = bookId,
                currentPage = currentPage,
                totalPages = totalPages,
                percentage = percentage,
                status = newStatus,
                startedAt = existing?.startedAt ?: now,
                finishedAt = if (newStatus == ReadingStatus.FINISHED.name) now else existing?.finishedAt,
                updatedAt = now
            )
            readingProgressDao.upsertProgress(updatedEntity)
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error(AppError.Database.WriteFailed(e))
        }
    }

    override suspend fun updateReadingStatus(
        bookId: String,
        status: ReadingStatus
    ): AppResult<Unit> = withContext(dispatchers.io) {
        try {
            val now = DateUtils.nowEpochMillis()
            val existing = readingProgressDao.getProgressForBook(bookId)
            val updatedEntity = ReadingProgressEntity(
                bookId = bookId,
                currentPage = existing?.currentPage ?: 0,
                totalPages = existing?.totalPages ?: 0,
                percentage = existing?.percentage ?: 0.0,
                status = status.name,
                startedAt = existing?.startedAt ?: if (status == ReadingStatus.READING) now else null,
                finishedAt = if (status == ReadingStatus.FINISHED) now else existing?.finishedAt,
                updatedAt = now
            )
            readingProgressDao.upsertProgress(updatedEntity)
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error(AppError.Database.WriteFailed(e))
        }
    }

    override suspend fun deleteBook(id: String): AppResult<Unit> = withContext(dispatchers.io) {
        try {
            bookDao.deleteBookById(id)
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error(AppError.Database.WriteFailed(e))
        }
    }

    override fun observeNotes(bookId: String): Flow<List<Note>> {
        return noteQuoteDao.observeNotesForBook(bookId).map { list ->
            list.map { it.toDomain() }
        }.flowOn(dispatchers.io)
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun addNote(
        bookId: String,
        content: String,
        pageNumber: Int?
    ): AppResult<Unit> = withContext(dispatchers.io) {
        try {
            val now = DateUtils.nowEpochMillis()
            val noteEntity = NoteEntity(
                id = Uuid.random().toString(),
                bookId = bookId,
                content = content,
                pageNumber = pageNumber,
                createdAt = now,
                updatedAt = now
            )
            noteQuoteDao.insertNote(noteEntity)
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error(AppError.Database.WriteFailed(e))
        }
    }

    override suspend fun deleteNote(noteId: String): AppResult<Unit> = withContext(dispatchers.io) {
        try {
            noteQuoteDao.deleteNote(noteId)
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error(AppError.Database.WriteFailed(e))
        }
    }

    override fun observeQuotes(bookId: String): Flow<List<Quote>> {
        return noteQuoteDao.observeQuotesForBook(bookId).map { list ->
            list.map { it.toDomain() }
        }.flowOn(dispatchers.io)
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun addQuote(
        bookId: String,
        text: String,
        pageNumber: Int?
    ): AppResult<Unit> = withContext(dispatchers.io) {
        try {
            val now = DateUtils.nowEpochMillis()
            val quoteEntity = QuoteEntity(
                id = Uuid.random().toString(),
                bookId = bookId,
                text = text,
                pageNumber = pageNumber,
                createdAt = now
            )
            noteQuoteDao.insertQuote(quoteEntity)
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error(AppError.Database.WriteFailed(e))
        }
    }

    override suspend fun deleteQuote(quoteId: String): AppResult<Unit> = withContext(dispatchers.io) {
        try {
            noteQuoteDao.deleteQuote(quoteId)
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error(AppError.Database.WriteFailed(e))
        }
    }
}
