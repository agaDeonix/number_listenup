package com.pinkunicorp.voicenumbers.ui.screens.training

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinkunicorp.voicenumbers.data.model.NumberType
import com.pinkunicorp.voicenumbers.data.model.NumberVariantState
import com.pinkunicorp.voicenumbers.data.repository.SettingsRepository
import com.pinkunicorp.voicenumbers.extentions.toOrdinalNumberString
import com.pinkunicorp.voicenumbers.extentions.toRationalString
import com.pinkunicorp.voicenumbers.extentions.toWholeNumberString
import com.pinkunicorp.voicenumbers.ui.elements.Key
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class TrainingEvent {
    object GoToBack : TrainingEvent()
    object GoToSettings : TrainingEvent()
    object NoneTypesForTrainings : TrainingEvent()
    data class PlayNumber(val number: String) : TrainingEvent()
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

class TrainingViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {

    sealed class TrainingNumber(val type: NumberType) {
        data class WholeNumber(val number: Long) : TrainingNumber(NumberType.WHOLE) {
            override fun getTrainingValue() = number.toWholeNumberString()
            override fun getCorrectValue() = number.toString()
        }

        data class OrdinalNumber(val number: Long) : TrainingNumber(NumberType.ORDINAL) {
            override fun getTrainingValue() = number.toOrdinalNumberString()
            override fun getCorrectValue() = number.toString()
        }

        data class RationalNumber(val number: Float) : TrainingNumber(NumberType.RATIONAL) {
            override fun getTrainingValue() = number.toRationalString()
            override fun getCorrectValue() = number.toString()
        }

        data class FractionNumber(val numerator: Long, val denominator: Long) :
            TrainingNumber(NumberType.FRACTION) {
            override fun getTrainingValue() =
                numerator.toWholeNumberString() + " " + denominator.toOrdinalNumberString()

            override fun getCorrectValue() = "$numerator/$denominator"
        }

        data class DateNumber(val date: Long) : TrainingNumber(NumberType.DATE) {
            override fun getTrainingValue() = date.toString()//FIXME need rework
            override fun getCorrectValue() = date.toString()
        }

        abstract fun getTrainingValue(): String
        abstract fun getCorrectValue(): String
    }

    private var targetNumber: TrainingNumber? = null
    private var numberVariantForGenerate: List<NumberVariantState> = emptyList()

    fun onStart() {
        viewModelScope.launch {
            delay(500)
            val newStates = settingsRepository.getNumberVariantStates().filter { it.isEnable }
            var needRegenerate = false
            if (numberVariantForGenerate.isEmpty()) {
                numberVariantForGenerate = newStates
            } else {
                if (numberVariantForGenerate != newStates) {
                    numberVariantForGenerate = newStates
                    needRegenerate = true
                }
            }
            if (numberVariantForGenerate.isEmpty()) {
                targetNumber = null
                _uiState.update {
                    it.copy(events = it.events + TrainingEvent.NoneTypesForTrainings)
                }
            } else {
                if (targetNumber == null || needRegenerate) {
                    targetNumber = generateNumber()
                    _uiState.update {
                        it.copy(
                            events = it.events + TrainingEvent.PlayNumber(
                                targetNumber?.getTrainingValue() ?: ""
                            )
                        )
                    }
                }
            }
        }
    }

    fun onStop() {
        Log.e("TRAINING", "onStop")
    }

    private val _uiState = MutableStateFlow(TrainingState())
    val uiState = _uiState.asStateFlow()


    private fun generateNumber(): TrainingNumber {
        val numberVariant = numberVariantForGenerate.random()
        return when (numberVariant.type) {
            NumberType.WHOLE -> TrainingNumber.WholeNumber(generateLong())
            NumberType.ORDINAL -> TrainingNumber.OrdinalNumber(generateLong())
            NumberType.RATIONAL -> TrainingNumber.RationalNumber(generateFloat())
            NumberType.FRACTION -> TrainingNumber.FractionNumber(
                (1L..999L).random(),
                (1L..999L).random()
            )
            NumberType.DATE -> TrainingNumber.DateNumber((1L..9999L).random())
        }

    }

    private fun generateFloat(): Float {
        val denominator = (1..9999).random()
        return ((0..100).random().toString() + "." + denominator.toString()).toFloat()
    }

    private fun generateLong(): Long {
        return when ((0..100).random()) {
            in 0..10 -> (10_000L..999_999L).random()
            in 11..20 -> (1000L..9_999L).random()
            in 21..50 -> (100L..999L).random()
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
        targetNumber?.let { it ->
            if (it.getCorrectValue() == _uiState.value.currentNumber) {
                showCorrect()
            } else {
                showError()
            }
        }
    }

    private fun showError() {
        _uiState.update {
            it.copy(
                fieldState = TrainingFieldState.Error
            )
        }
    }

    private fun showCorrect() {
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
                    events = it.events + TrainingEvent.PlayNumber(targetNumber?.getTrainingValue() ?: "")
                )
            }
        }
    }

    fun onRepeatClick() {
        if (targetNumber == null) {
            _uiState.update {
                it.copy(
                    events = it.events + TrainingEvent.NoneTypesForTrainings
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    events = it.events + TrainingEvent.PlayNumber(
                        targetNumber?.getTrainingValue() ?: ""
                    )
                )
            }
        }
    }

    fun onSettingsClick() {
        _uiState.update {
            it.copy(events = it.events + TrainingEvent.GoToSettings)
        }
    }
}