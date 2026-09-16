package com.shelf.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthorDto(
    @SerialName("key")
    val key: String,
    @SerialName("name")
    val name: String,
    @SerialName("bio")
    @Serializable(with = DescriptionSerializer::class)
    val bio: String? = null,
    @SerialName("photos")
    val photos: List<Long> = emptyList()
)
