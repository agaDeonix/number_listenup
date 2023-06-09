package com.pinkunicorp.numberlistenup.ui.screens.training

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinkunicorp.numberlistenup.data.model.NumberType
import com.pinkunicorp.numberlistenup.data.model.NumberVariantState
import com.pinkunicorp.numberlistenup.data.repository.ScoreRepository
import com.pinkunicorp.numberlistenup.data.repository.SettingsRepository
import com.pinkunicorp.numberlistenup.extentions.toOrdinalNumberString
import com.pinkunicorp.numberlistenup.extentions.toRationalAndString
import com.pinkunicorp.numberlistenup.extentions.toRationalPointString
import com.pinkunicorp.numberlistenup.extentions.toWholeNumberString
import com.pinkunicorp.numberlistenup.ui.elements.Key
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
    data class Correct(val prize: Int) : TrainingFieldState()
    object Answer : TrainingFieldState()
    object Error : TrainingFieldState()
}

class TrainingViewModel(
    private val scoreRepository: ScoreRepository,
    private val settingsRepository: SettingsRepository,
    ) : ViewModel() {

    sealed class TrainingNumber {
        data class WholeNumber(val number: Long) : TrainingNumber() {
            override fun getTrainingValue() = number.toWholeNumberString()
            override fun getCorrectValue() = number.toString()
            override fun getScorePrize() = 1
            override fun checkValue(value: String): Boolean {
                return getCorrectValue() == value
            }
        }

        data class OrdinalNumber(val number: Long) : TrainingNumber() {
            override fun getTrainingValue() = number.toOrdinalNumberString()
            override fun getCorrectValue() = number.toString()
            override fun getScorePrize() = 3
            override fun checkValue(value: String): Boolean {
                return getCorrectValue() == value
            }
        }

        data class RationalNumber(val number: Float) : TrainingNumber() {
            override fun getTrainingValue() = when ((0..1).random()) {
                0 -> number.toRationalAndString()
                else -> number.toRationalPointString()
            }

            override fun getCorrectValue() = number.toString()
            override fun getScorePrize() = 5
            override fun checkValue(value: String): Boolean {
                return getCorrectValue() == value
            }
        }

        data class FractionNumber(val whole: Long, val numerator: Long, val denominator: Long) :
            TrainingNumber() {
            override fun getTrainingValue(): String {
                var result = ""
                if (whole > 0L) {
                    result = whole.toWholeNumberString() + " and "
                }
                result += if (numerator == 1L) {
                    when (denominator) {
                        2L -> "a half"
                        4L -> "a quarter"
                        else -> "a " + denominator.toOrdinalNumberString()
                    }
                } else {
                    numerator.toWholeNumberString() + " " + denominator.toOrdinalNumberString()
                }
                return result
            }

            override fun getCorrectValue() = "$whole $numerator/$denominator"
            override fun getScorePrize() = 5
            override fun checkValue(value: String): Boolean {
                return getCorrectValue() == value
            }
        }

        data class TimeNumber(val hour: Long, val minute: Long) : TrainingNumber() {
            override fun getTrainingValue() = getTimeTrainingValue(hour, minute)

            private fun getTimeTrainingValue(hour: Long, minute: Long): String {
                if (minute == 0L) {
                    return hour.toWholeNumberString() + " hundred" + " hours"
                } else {
                    if (minute < 30) {
                        if (minute == 15L) {
                            return "quarter past " + hour.toWholeNumberString()
                        } else {
                            if (minute == 1L) {
                                return "one minute past " + hour.toWholeNumberString()
                            } else if (minute <= 5L) {
                                return minute.toWholeNumberString() + " minutes past " + hour.toWholeNumberString()
                            }
                            return minute.toWholeNumberString() + " past " + hour.toWholeNumberString()
                        }
                    } else if (minute > 30L) {
                        if (minute == 45L) {
                            return "quarter to " + (hour + 1).toWholeNumberString()
                        } else {
                            if (minute == 59L) {
                                return "one minute to " + (hour + 1).toWholeNumberString()
                            } else if (minute >= 55L) {
                                return (60 - minute).toWholeNumberString() + " minutes to " + (hour + 1).toWholeNumberString()
                            }
                            return (60 - minute).toWholeNumberString() + " to " + (hour + 1).toWholeNumberString()
                        }
                    } else {
                        return "half past " + hour.toWholeNumberString()
                    }
                }
            }

            override fun getCorrectValue(): String {
                val hourString = if (hour < 10) "0$hour" else hour.toString()
                val minuteString = if (minute < 10) "0$minute" else minute.toString()
                return "$hourString:$minuteString"
            }
            override fun getScorePrize() = 7
            override fun checkValue(value: String): Boolean {
                val hours = value.split(":").getOrNull(0) ?: ""
                return if (hours.length < 2) {
                    getCorrectValue() == "0$value"
                } else {
                    getCorrectValue() == value
                }
            }
        }

        abstract fun getTrainingValue(): String
        abstract fun checkValue(value: String): Boolean
        abstract fun getCorrectValue(): String
        abstract fun getScorePrize(): Int
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
                (0L..999L).random(),
                (1L..999L).random(),
                (1L..999L).random()
            )
            NumberType.TIME -> TrainingNumber.TimeNumber((0L..23L).random(), (0L..59L).random())
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
            Key.Colon -> _uiState.update {
                it.copy(
                    currentNumber = it.currentNumber + ":",
                    fieldState = TrainingFieldState.Normal
                )
            }
            Key.Space -> _uiState.update {
                it.copy(
                    currentNumber = it.currentNumber + " ",
                    fieldState = TrainingFieldState.Normal
                )
            }
        }
    }

    private fun checkValue() {
        targetNumber?.let { it ->
            if (it.checkValue(_uiState.value.currentNumber)) {
                viewModelScope.launch {
                    scoreRepository.addScore(it.getScorePrize())
                }
                showCorrect(it.getScorePrize())
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

    fun onAnswerClick() {
        viewModelScope.launch {
            targetNumber?.let { target ->
                _uiState.update {
                    it.copy(
                        currentNumber = target.getCorrectValue(),
                        fieldState = TrainingFieldState.Answer
                    )
                }
                delay(3000L)
                targetNumber = generateNumber()
                _uiState.update {
                    it.copy(
                        currentNumber = "",
                        fieldState = TrainingFieldState.Normal,
                        events = it.events + TrainingEvent.PlayNumber(
                            targetNumber?.getTrainingValue() ?: ""
                        )
                    )
                }
            }
        }
    }

    private fun showCorrect(prize: Int) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    fieldState = TrainingFieldState.Correct(prize)
                )
            }
            delay(500L)
            targetNumber = generateNumber()
            _uiState.update {
                it.copy(
                    currentNumber = "",
                    fieldState = TrainingFieldState.Normal,
                    events = it.events + TrainingEvent.PlayNumber(
                        targetNumber?.getTrainingValue() ?: ""
                    )
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