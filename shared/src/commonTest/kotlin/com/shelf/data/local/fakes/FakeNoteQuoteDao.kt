package com.shelf.data.local.fakes

import com.shelf.data.local.dao.NoteQuoteDao
import com.shelf.data.local.entity.NoteEntity
import com.shelf.data.local.entity.QuoteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeNoteQuoteDao : NoteQuoteDao {
    private val notesMap = MutableStateFlow<Map<String, NoteEntity>>(emptyMap())
    private val quotesMap = MutableStateFlow<Map<String, QuoteEntity>>(emptyMap())

    override suspend fun insertNote(note: NoteEntity) {
        notesMap.value = notesMap.value + (note.id to note)
    }

    override fun observeNotesForBook(bookId: String): Flow<List<NoteEntity>> {
        return notesMap.map { map -> map.values.filter { it.bookId == bookId } }
    }

    override suspend fun deleteNote(id: String) {
        notesMap.value = notesMap.value - id
    }

    override suspend fun insertQuote(quote: QuoteEntity) {
        quotesMap.value = quotesMap.value + (quote.id to quote)
    }

    override fun observeQuotesForBook(bookId: String): Flow<List<QuoteEntity>> {
        return quotesMap.map { map -> map.values.filter { it.bookId == bookId } }
    }

    override suspend fun deleteQuote(id: String) {
        quotesMap.value = quotesMap.value - id
    }
}
