package com.shelf.personal.book.ui.screens.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shelf.core.utils.DateUtils
import com.shelf.domain.model.ReadingGoal
import com.shelf.domain.model.ReadingStatistics
import com.shelf.domain.usecase.GetReadingGoalUseCase
import com.shelf.domain.usecase.GetReadingStatisticsUseCase
import com.shelf.domain.usecase.SetReadingGoalUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class StatsUiState(
    val statistics: ReadingStatistics = ReadingStatistics(),
    val currentGoal: ReadingGoal? = null,
    val isLoading: Boolean = true
)

class StatsViewModel(
    getReadingStatisticsUseCase: GetReadingStatisticsUseCase,
    getReadingGoalUseCase: GetReadingGoalUseCase,
    private val setReadingGoalUseCase: SetReadingGoalUseCase
) : ViewModel() {

    private val currentYear = DateUtils.currentYear()

    val uiState: StateFlow<StatsUiState> = combine(
        getReadingStatisticsUseCase(),
        getReadingGoalUseCase(currentYear)
    ) { stats, goal ->
        StatsUiState(
            statistics = stats,
            currentGoal = goal,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = StatsUiState(isLoading = true)
    )

    fun updateGoal(targetBooks: Int) {
        viewModelScope.launch {
            setReadingGoalUseCase(currentYear, targetBooks)
        }
    }
}
