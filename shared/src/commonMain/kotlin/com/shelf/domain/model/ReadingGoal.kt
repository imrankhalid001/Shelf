package com.shelf.domain.model

data class ReadingGoal(
    val id: String,
    val year: Int,
    val targetBooks: Int,
    val completedBooks: Int = 0,
    val progressPercentage: Double = 0.0
)
