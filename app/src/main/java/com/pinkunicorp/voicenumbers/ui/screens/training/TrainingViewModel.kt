package com.pinkunicorp.voicenumbers.ui.screens.training

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinkunicorp.voicenumbers.ui.elements.Key
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class TrainingEvent {
    object GoToBack : TrainingEvent()
    data class PlayNumber(val number: Long) : TrainingEvent()
}

data class TrainingState(
    val currentNumber: String = "",
    val fieldState: TrainingFieldState = TrainingFieldState.Normal,
    val events: List<TrainingEvent> = emptyList()
)

sealed class TrainingFieldState {
    object Normal : TrainingFieldState()
    object Correct : TrainingFieldState()
    object Error : TrainingFieldState()
}

class TrainingViewModel : ViewModel() {

    private var targetNumber = 0L

    init {
        viewModelScope.launch {
            delay(1000)
            targetNumber = generateNumber()
            _uiState.update {
                it.copy(
                    events = it.events + TrainingEvent.PlayNumber(targetNumber)
                )
            }
        }
    }

    private val _uiState = MutableStateFlow(TrainingState())
    val uiState = _uiState.asStateFlow()


    private fun generateNumber(): Long {
        val chance = (0..100).random()
        return when (chance) {
            in 0..3 -> (1_000_000_000L..999_999_999_999L).random()
            in 4..10 -> (1_000_000L..999_999_999L).random()
            in 11..20 -> (10_000L..999_999L).random()
            in 21..50 -> (100L..9_999L).random()
            else -> (0L..99L).random()
        }
    }

    fun onBackClick() {
        _uiState.update {
            it.copy(events = it.events + TrainingEvent.GoToBack)
        }
    }

    fun consumeEvents(events: List<TrainingEvent>) {
        _uiState.update {
            it.copy(events = it.events - events.toSet())
        }
    }

    fun onNumberKeyClick(key: Key) {
        when (key) {
            is Key.Number -> {
                _uiState.update {
                    it.copy(
                        currentNumber = it.currentNumber + key.value.toString(),
                        fieldState = TrainingFieldState.Normal
                    )
                }
            }
            is Key.Backspace -> {
                _uiState.update {
                    it.copy(
                        currentNumber = it.currentNumber.dropLast(1),
                        fieldState = TrainingFieldState.Normal
                    )
                }
            }
            Key.Done -> checkValue()
            Key.Dot -> _uiState.update {
                it.copy(
                    currentNumber = it.currentNumber + ".",
                    fieldState = TrainingFieldState.Normal
                )
            }
            Key.Slash -> _uiState.update {
                it.copy(
                    currentNumber = it.currentNumber + "/",
                    fieldState = TrainingFieldState.Normal
                )
            }
        }
    }

    private fun checkValue() {
        if ((_uiState.value.currentNumber.toLongOrNull() ?: 0) == targetNumber) {
            viewModelScope.launch {
                _uiState.update {
                    it.copy(
                        fieldState = TrainingFieldState.Correct
                    )
                }
                delay(500L)
                targetNumber = generateNumber()
                _uiState.update {
                    it.copy(
                        currentNumber = "",
                        fieldState = TrainingFieldState.Normal,
                        events = it.events + TrainingEvent.PlayNumber(targetNumber)
                    )
                }
            }
        } else {
            _uiState.update {
                it.copy(
                    fieldState = TrainingFieldState.Error
                )
            }
        }
    }

    fun onRepeatClick() {
        _uiState.update {
            it.copy(
                events = it.events + TrainingEvent.PlayNumber(targetNumber)
            )
        }
    }
}