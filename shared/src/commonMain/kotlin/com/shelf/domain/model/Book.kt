package com.shelf.domain.model

data class Book(
    val id: String,
    val workId: String,
    val title: String,
    val subtitle: String? = null,
    val description: String? = null,
    val coverId: Long? = null,
    val coverUrl: String? = null,
    val authors: List<Author> = emptyList(),
    val firstPublishYear: Int? = null,
    val pageCount: Int = 0,
    val isbn10: String? = null,
    val isbn13: String? = null,
    val subjects: List<String> = emptyList(),
    val publisher: String? = null,
    val language: String = "en",
    val readingProgress: ReadingProgress? = null,
    val isFavorite: Boolean = false,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)
