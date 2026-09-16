package com.shelf.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shelf.data.local.entity.CollectionBookCrossRef
import com.shelf.data.local.entity.CollectionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollection(collection: CollectionEntity)

    @Query("SELECT * FROM collections ORDER BY name ASC")
    fun observeAllCollections(): Flow<List<CollectionEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addBookToCollection(crossRef: CollectionBookCrossRef)

    @Query("DELETE FROM collection_books WHERE collection_id = :collectionId AND book_id = :bookId")
    suspend fun removeBookFromCollection(collectionId: String, bookId: String)

    @Query("DELETE FROM collections WHERE id = :id")
    suspend fun deleteCollection(id: String)
}
