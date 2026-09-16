package com.shelf.data.remote.api

import com.shelf.core.error.AppError
import com.shelf.core.result.AppResult
import com.shelf.data.remote.dto.AuthorDto
import com.shelf.data.remote.dto.SearchResponseDto
import com.shelf.data.remote.dto.WorkDetailsDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.JsonConvertException

class OpenLibraryApiImpl(
    private val client: HttpClient
) : OpenLibraryApi {

    companion object {
        private const val BASE_URL = "https://openlibrary.org"
    }

    override suspend fun searchBooks(
        query: String,
        page: Int,
        limit: Int
    ): AppResult<SearchResponseDto> = safeApiCall {
        client.get("$BASE_URL/search.json") {
            parameter("q", query)
            parameter("page", page)
            parameter("limit", limit)
            parameter("fields", "key,title,author_name,author_key,first_publish_year,cover_i,edition_count,isbn,subject")
        }.body()
    }

    override suspend fun getWorkDetails(workId: String): AppResult<WorkDetailsDto> = safeApiCall {
        val cleanId = workId.removePrefix("/works/").removePrefix("works/")
        client.get("$BASE_URL/works/$cleanId.json").body()
    }

    override suspend fun getAuthorDetails(authorId: String): AppResult<AuthorDto> = safeApiCall {
        val cleanId = authorId.removePrefix("/authors/").removePrefix("authors/")
        client.get("$BASE_URL/authors/$cleanId.json").body()
    }

    private inline fun <T> safeApiCall(block: () -> T): AppResult<T> {
        return try {
            AppResult.Success(block())
        } catch (e: ResponseException) {
            val code = e.response.status.value
            AppResult.Error(AppError.Network.ServerError(code = code, message = e.message))
        } catch (e: JsonConvertException) {
            AppResult.Error(AppError.Network.Serialization(message = e.message ?: "JSON parse error"))
        } catch (e: Exception) {
            AppResult.Error(AppError.Network.NoInternet)
        }
    }
}
