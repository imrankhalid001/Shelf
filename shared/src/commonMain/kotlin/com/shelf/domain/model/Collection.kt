package com.shelf.domain.model

data class Collection(
    val id: String,
    val name: String,
    val description: String? = null,
    val coverBookId: String? = null,
    val bookCount: Int = 0,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)
