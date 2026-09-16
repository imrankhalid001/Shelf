package com.shelf.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shelf.data.local.entity.AuthorEntity

@Dao
interface AuthorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(author: AuthorEntity)

    @Query("SELECT * FROM authors WHERE id = :id")
    suspend fun getAuthorById(id: String): AuthorEntity?
}
