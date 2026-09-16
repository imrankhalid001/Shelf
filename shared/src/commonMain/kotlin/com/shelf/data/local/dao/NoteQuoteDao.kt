package com.shelf.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shelf.data.local.entity.NoteEntity
import com.shelf.data.local.entity.QuoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteQuoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity)

    @Query("SELECT * FROM notes WHERE book_id = :bookId ORDER BY page_number ASC, created_at DESC")
    fun observeNotesForBook(bookId: String): Flow<List<NoteEntity>>

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteNote(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: QuoteEntity)

    @Query("SELECT * FROM quotes WHERE book_id = :bookId ORDER BY page_number ASC, created_at DESC")
    fun observeQuotesForBook(bookId: String): Flow<List<QuoteEntity>>

    @Query("DELETE FROM quotes WHERE id = :id")
    suspend fun deleteQuote(id: String)
}
