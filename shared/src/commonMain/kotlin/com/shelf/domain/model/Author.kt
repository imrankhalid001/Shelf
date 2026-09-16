package com.shelf.domain.model

data class Author(
    val id: String,
    val name: String,
    val bio: String? = null,
    val photoUrl: String? = null
)
