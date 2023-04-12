package com.pinkunicorp.numberlistenup.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinkunicorp.numberlistenup.data.model.NumberVariantState
import com.pinkunicorp.numberlistenup.data.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class SettingsEvent {
    object GoToBack : SettingsEvent()
}

data class SettingsState(
    val numberVariants: List<NumberVariantState> = emptyList(),
    val events: List<SettingsEvent> = emptyList()
)

class SettingsViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    numberVariants = settingsRepository.getNumberVariantStates()
                )
            }
        }
    }

    fun onBackClick() {
        _uiState.update {
            it.copy(events = it.events + SettingsEvent.GoToBack)
        }
    }

    fun consumeEvents(events: List<SettingsEvent>) {
        _uiState.update {
            it.copy(events = it.events - events.toSet())
        }
    }

    fun onNumberVariantChanged(numberVariant: NumberVariantState, enable: Boolean) {
        viewModelScope.launch {
            if (enable) {
                settingsRepository.enableNumberVariant(numberVariant)
            } else {
                settingsRepository.disableNumberVariant(numberVariant)
            }
            _uiState.update {
                it.copy(
                    numberVariants = settingsRepository.getNumberVariantStates()
                )
            }
        }
    }

}