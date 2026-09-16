package com.shelf.domain.usecase

import com.shelf.domain.model.ReadingStatistics
import com.shelf.domain.repository.StatsRepository
import kotlinx.coroutines.flow.Flow

class GetReadingStatisticsUseCase(
    private val statsRepository: StatsRepository
) {
    operator fun invoke(): Flow<ReadingStatistics> {
        return statsRepository.observeStatistics()
    }
}
