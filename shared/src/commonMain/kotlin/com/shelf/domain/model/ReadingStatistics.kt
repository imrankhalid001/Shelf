package com.shelf.domain.model

data class ReadingStatistics(
    val totalBooksReadThisYear: Int = 0,
    val totalPagesRead: Int = 0,
    val totalReadingTimeMinutes: Long = 0L,
    val currentStreakDays: Int = 0,
    val longestStreakDays: Int = 0,
    val favoriteGenres: List<Pair<String, Int>> = emptyList(),
    val averageBooksPerMonth: Double = 0.0
)
