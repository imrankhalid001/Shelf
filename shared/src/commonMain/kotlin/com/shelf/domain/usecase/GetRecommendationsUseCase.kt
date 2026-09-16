package com.shelf.domain.usecase

import com.shelf.domain.model.Book
import com.shelf.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetRecommendationsUseCase(
    private val bookRepository: BookRepository
) {
    operator fun invoke(): Flow<List<Book>> {
        return bookRepository.observeLibrary().map { library ->
            if (library.isEmpty()) {
                emptyList()
            } else {
                val favoriteSubjects = library
                    .flatMap { it.subjects }
                    .filter { it.isNotBlank() }
                    .groupingBy { it }
                    .eachCount()
                    .entries
                    .sortedByDescending { it.value }
                    .map { it.key }
                    .take(3)
                    .toSet()

                library.filter { book ->
                    book.subjects.any { subject -> subject in favoriteSubjects }
                }.take(5)
            }
        }
    }
}
