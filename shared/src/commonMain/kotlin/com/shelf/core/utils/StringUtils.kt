package com.shelf.core.utils

object StringUtils {
    fun cleanIsbn(rawIsbn: String): String {
        return rawIsbn.replace("-", "").replace(" ", "").trim()
    }

    fun formatReadingPercentage(currentPage: Int, totalPages: Int): Double {
        if (totalPages <= 0) return 0.0
        val percentage = (currentPage.toDouble() / totalPages.toDouble()) * 100.0
        return percentage.coerceIn(0.0, 100.0)
    }

    fun extractOpenLibraryId(key: String): String {
        return key.removePrefix("/works/").removePrefix("/authors/").trim()
    }
}
