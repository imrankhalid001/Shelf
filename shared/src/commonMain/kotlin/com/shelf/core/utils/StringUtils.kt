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

    fun formatReadingPace(pagesRead: Int, durationMinutes: Long): String {
        if (durationMinutes <= 0 || pagesRead <= 0) return "0.0"
        val pacePerHour = (pagesRead.toDouble() / durationMinutes.toDouble()) * 60.0
        val integerPart = pacePerHour.toInt()
        val decimalPart = ((pacePerHour - integerPart) * 10).toInt()
        return "$integerPart.$decimalPart"
    }
}
