package com.pinkunicorp.voicenumbers.ui.screens.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

sealed class HomeEvent {
    object GoToTraining: HomeEvent()
}

data class HomeState(
    val bestScore: Int = 0,
    val events: List<HomeEvent> = emptyList()
)

class HomeViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()

    init {
        _uiState.value = HomeState(
            bestScore = 0//TODO need read best score from repository
        )
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