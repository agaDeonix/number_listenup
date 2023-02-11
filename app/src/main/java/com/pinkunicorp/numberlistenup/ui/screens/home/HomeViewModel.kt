package com.pinkunicorp.numberlistenup.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinkunicorp.numberlistenup.data.repository.ScoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class HomeEvent {
    object GoToTraining: HomeEvent()
}

data class HomeState(
    val score: Int = 0,
    val events: List<HomeEvent> = emptyList()
)

class HomeViewModel(private val scoreRepository: ScoreRepository): ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            scoreRepository.getScore().collect { score ->
                _uiState.update {
                    it.copy(score = score)
                }
            }
        }

    }

    fun onTrainingClick() {
        _uiState.update {
            it.copy(
                events = it.events + HomeEvent.GoToTraining
            )
        }
    }

    fun consumeEvents(events: List<HomeEvent>) {
        _uiState.update {
            it.copy(events = it.events - events.toSet())
        }
    }


}