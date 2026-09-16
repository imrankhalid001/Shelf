package com.shelf.domain.usecase

import com.shelf.core.error.AppError
import com.shelf.core.result.AppResult
import com.shelf.domain.repository.StatsRepository

class SetReadingGoalUseCase(
    private val statsRepository: StatsRepository
) {
    suspend operator fun invoke(year: Int, targetBooks: Int): AppResult<Unit> {
        if (targetBooks <= 0) {
            return AppResult.Error(AppError.Validation.Custom("Target books must be greater than zero."))
        }
        return statsRepository.setGoal(year, targetBooks)
    }
}
