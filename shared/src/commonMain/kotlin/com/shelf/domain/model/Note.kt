package com.shelf.domain.model

data class Note(
    val id: String,
    val bookId: String,
    val content: String,
    val pageNumber: Int? = null,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)
