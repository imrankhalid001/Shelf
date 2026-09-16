package com.shelf.data.remote

import com.shelf.core.result.getOrNull
import com.shelf.data.remote.api.OpenLibraryApiImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class OpenLibraryApiTest {

    @Test
    fun searchBooks_returnsDeserializedResponse() = runTest {
        val mockJson = """
            {
              "numFound": 1,
              "start": 0,
              "docs": [
                {
                  "key": "/works/OL27517W",
                  "title": "Atomic Habits",
                  "author_name": ["James Clear"],
                  "author_key": ["OL7510103A"],
                  "first_publish_year": 2018,
                  "cover_i": 10522434
                }
              ]
            }
        """.trimIndent()

        val engine = MockEngine {
            respond(
                content = mockJson,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val client = HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        val api = OpenLibraryApiImpl(client)
        val result = api.searchBooks("Atomic Habits")

        assertTrue(result.isSuccess)
        val response = result.getOrNull()
        assertNotNull(response)
        assertEquals(1, response.numFound)
        assertEquals("Atomic Habits", response.docs.first().title)
        assertEquals("James Clear", response.docs.first().authorNames.first())
    }
}
