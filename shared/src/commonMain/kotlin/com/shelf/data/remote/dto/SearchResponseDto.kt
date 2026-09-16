package com.shelf.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponseDto(
    @SerialName("numFound")
    val numFound: Int = 0,
    @SerialName("start")
    val start: Int = 0,
    @SerialName("docs")
    val docs: List<SearchBookDocDto> = emptyList()
)

@Serializable
data class SearchBookDocDto(
    @SerialName("key")
    val key: String,
    @SerialName("title")
    val title: String,
    @SerialName("author_name")
    val authorNames: List<String> = emptyList(),
    @SerialName("author_key")
    val authorKeys: List<String> = emptyList(),
    @SerialName("first_publish_year")
    val firstPublishYear: Int? = null,
    @SerialName("cover_i")
    val coverId: Long? = null,
    @SerialName("edition_count")
    val editionCount: Int = 0,
    @SerialName("isbn")
    val isbns: List<String> = emptyList(),
    @SerialName("subject")
    val subjects: List<String> = emptyList()
)
