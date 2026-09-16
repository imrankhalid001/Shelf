package com.shelf.domain.model

enum class ReadingStatus {
    WANT_TO_READ,
    READING,
    FINISHED,
    PAUSED,
    DROPPED;

    companion object {
        fun fromString(value: String?): ReadingStatus {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: WANT_TO_READ
        }
    }
}
