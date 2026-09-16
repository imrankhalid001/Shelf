package com.shelf.domain.usecase

import com.shelf.domain.model.ReadingGoal
import com.shelf.domain.repository.StatsRepository
import kotlinx.coroutines.flow.Flow

class GetReadingGoalUseCase(
    private val statsRepository: StatsRepository
) {
    operator fun invoke(year: Int): Flow<ReadingGoal?> {
        return statsRepository.observeGoal(year)
    }
}
