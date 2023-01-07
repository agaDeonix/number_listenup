package com.pinkunicorp.voicenumbers.ui.screens.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

sealed class SettingsEvent {
    object GoToBack : SettingsEvent()
}

data class SettingsState(
    val events: List<SettingsEvent> = emptyList()
)

class SettingsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsState())
    val uiState = _uiState.asStateFlow()

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

}