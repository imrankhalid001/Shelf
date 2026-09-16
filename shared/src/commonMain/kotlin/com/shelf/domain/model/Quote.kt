package com.shelf.domain.model

data class Quote(
    val id: String,
    val bookId: String,
    val text: String,
    val pageNumber: Int? = null,
    val createdAt: Long = 0L
)
