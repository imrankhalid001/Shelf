package com.shelf.data.remote.api

import com.shelf.core.result.AppResult
import com.shelf.data.remote.dto.AuthorDto
import com.shelf.data.remote.dto.SearchResponseDto
import com.shelf.data.remote.dto.WorkDetailsDto

interface OpenLibraryApi {
    suspend fun searchBooks(
        query: String,
        page: Int = 1,
        limit: Int = 20
    ): AppResult<SearchResponseDto>

    suspend fun getWorkDetails(workId: String): AppResult<WorkDetailsDto>

    suspend fun getAuthorDetails(authorId: String): AppResult<AuthorDto>
}
