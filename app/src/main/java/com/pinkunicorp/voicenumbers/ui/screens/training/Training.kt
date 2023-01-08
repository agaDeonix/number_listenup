package com.pinkunicorp.voicenumbers.ui.screens.training

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pinkunicorp.voicenumbers.R
import com.pinkunicorp.voicenumbers.other.tts.TTSService
import com.pinkunicorp.voicenumbers.ui.elements.Key
import com.pinkunicorp.voicenumbers.ui.elements.NumberFieldView
import com.pinkunicorp.voicenumbers.ui.elements.NumbersKeyboard
import com.pinkunicorp.voicenumbers.ui.elements.RepeatListeningButton
import com.pinkunicorp.voicenumbers.ui.screens.Screen
import org.koin.androidx.compose.get
import org.koin.androidx.compose.koinViewModel

@Composable
fun TrainingScreen(
    navController: NavController,
    trainingViewModel: TrainingViewModel = koinViewModel(),
    ttsService: TTSService = get()
) {
    val state by trainingViewModel.uiState.collectAsState()

    DisposableEffect(key1 = trainingViewModel) {
        trainingViewModel.onStart()
        onDispose { trainingViewModel.onStop() }
    }

    TrainingContent(
        state = state,
        onRepeatClick = {
            ttsService.stop()
            trainingViewModel.onRepeatClick()
        },
        onSettingsClick = {
            ttsService.stop()
            trainingViewModel.onSettingsClick()
        },
        onBackClick = {
            ttsService.stop()
            trainingViewModel.onBackClick()
        },
        onNumberKeyClick = { number ->
            trainingViewModel.onNumberKeyClick(number)
        },
    )
    state.events.forEach {
        when (it) {
            is TrainingEvent.GoToBack -> navController.navigateUp()
            is TrainingEvent.GoToSettings -> navController.navigate(Screen.Settings.route)
            is TrainingEvent.PlayNumber -> {
                playNumber(ttsService, it.number)
            }
        }
        trainingViewModel.consumeEvents(state.events)
    }
}

private fun playNumber(ttsService: TTSService, number: Long) {
    val listMediaPlayer = mutableListOf<String?>()
    val billion = number / 1000000000
    val million = (number - billion * 1000000000) / 1000000
    val thousand = (number - billion * 1000000000 - million * 1000000) / 1000
    val hundred =
        (number - billion * 1000000000 - million * 1000000 - thousand * 1000) / 100
    val ten = if (number % 100 <= 20) 0 else
        (number - billion * 1000000000 - million * 1000000 - thousand * 1000 - hundred * 100) / 10
    val one = if (number % 100 <= 20) number % 100 else
        number - billion * 1000000000 - million * 1000000 - thousand * 1000 - hundred * 100 - ten * 10

    if (billion > 0) {
        val billionHundreds = billion / 100
        val billionTens =
            if (billion % 100 <= 20) 0 else (billion - billionHundreds * 100) / 10
        val billionOnes =
            if (billion % 100 <= 20) billion % 100 else billion - billionHundreds * 100 - billionTens * 10

        if (billionHundreds > 0) {
            listMediaPlayer.add(
                getMediaItem(
                    billionHundreds.toString()
                )
            )
            listMediaPlayer.add(getMediaItem("hundred"))
        }
        if (billionTens > 0) {
            listMediaPlayer.add(
                getMediaItem(
                    (billionTens * 10).toString()
                )
            )
        }
        if (billionOnes > 0) {
            listMediaPlayer.add(getMediaItem(billionOnes.toString()))
        }
        listMediaPlayer.add(getMediaItem("billion"))
    }
    if (million > 0) {
        val millionHundreds = million / 100
        val millionTens =
            if (million % 100 <= 20) 0 else (million - millionHundreds * 100) / 10
        val millionOnes =
            if (million % 100 <= 20) million % 100 else million - millionHundreds * 100 - millionTens * 10

        if (millionHundreds > 0) {
            listMediaPlayer.add(
                getMediaItem(
                    millionHundreds.toString()
                )
            )
            listMediaPlayer.add(getMediaItem("hundred"))
        }
        if (millionTens > 0) {
            listMediaPlayer.add(
                getMediaItem(
                    (millionTens * 10).toString()
                )
            )
        }
        if (millionOnes > 0) {
            listMediaPlayer.add(getMediaItem(millionOnes.toString()))
        }
        listMediaPlayer.add(getMediaItem("million"))
    }
    if (thousand > 0) {
        val thousandHundreds = thousand / 100
        val thousandTens =
            if (thousand % 100 <= 20) 0 else (thousand - thousandHundreds * 100) / 10
        val thousandOnes =
            if (thousand % 100 <= 20) thousand % 100 else thousand - thousandHundreds * 100 - thousandTens * 10

        if (thousandHundreds > 0) {
            listMediaPlayer.add(
                getMediaItem(
                    thousandHundreds.toString()
                )
            )
            listMediaPlayer.add(getMediaItem("hundred"))
        }
        if (thousandTens > 0) {
            listMediaPlayer.add(
                getMediaItem(
                    (thousandTens * 10).toString()
                )
            )
        }
        if (thousandOnes > 0) {
            listMediaPlayer.add(getMediaItem(thousandOnes.toString()))
        }
        listMediaPlayer.add(getMediaItem("thousand"))
    }
    if (hundred > 0) {
        listMediaPlayer.add(getMediaItem(hundred.toString()))
        listMediaPlayer.add(getMediaItem("hundred"))
    }
    if (ten > 0) {
        listMediaPlayer.add(getMediaItem((ten * 10).toString()))
    }
    if (one > 0) {
        listMediaPlayer.add(getMediaItem(one.toString()))
    }
    ttsService.speak(listMediaPlayer.filterNotNull().joinToString(" "))
}

fun getMediaItem(number: String): String? {
    return when (number) {
        "0" -> "zero"
        "1" -> "one"
        "2" -> "two"
        "3" -> "three"
        "4" -> "four"
        "5" -> "five"
        "6" -> "six"
        "7" -> "seven"
        "8" -> "eight"
        "9" -> "nine"
        "10" -> "ten"
        "11" -> "eleven"
        "12" -> "twelve"
        "13" -> "thirteen"
        "14" -> "fourteen"
        "15" -> "fifteen"
        "16" -> "sixteen"
        "17" -> "seventeen"
        "18" -> "eighteen"
        "19" -> "nineteen"
        "20" -> "twenty"
        "30" -> "thirty"
        "40" -> "fourty"
        "50" -> "fifty"
        "60" -> "sixty"
        "70" -> "seventy"
        "80" -> "eighty"
        "90" -> "ninety"
        "hundred" -> "hungred"
        "thousand" -> "thousand"
        "million" -> "million"
        "billion" -> "billion"
        "dot" -> "dot"
        else -> null
    }
}

@Composable
fun TrainingToolbarView(
    modifier: Modifier = Modifier,
    onSettingsClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(54.dp)
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .clickable { onBackClick() }
                .align(Alignment.CenterStart)
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_back),
                contentDescription = ""
            )
        }

        Text(
            text = stringResource(id = R.string.training_title),
            modifier = Modifier.align(Alignment.Center),
            style = TextStyle(
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .clickable { onSettingsClick() }
                .align(Alignment.CenterEnd)
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_settings),
                contentDescription = ""
            )
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun TrainingContent(
    state: TrainingState,
    onSettingsClick: () -> Unit,
    onBackClick: () -> Unit,
    onRepeatClick: () -> Unit,
    onNumberKeyClick: (Key) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        TrainingToolbarView(
            onSettingsClick = onSettingsClick,
            onBackClick = onBackClick
        )
        Box(modifier = Modifier.weight(1f)) {
            RepeatListeningButton(
                modifier = Modifier.fillMaxHeight(),
                onClick = {
                    onRepeatClick()
                })
        }
        NumberFieldView(
            modifier = Modifier.padding(8.dp),
            currentNumber = state.currentNumber,
            hasError = state.fieldState == TrainingFieldState.Error,
            isCorrect = state.fieldState == TrainingFieldState.Correct
        )
        NumbersKeyboard {
            onNumberKeyClick(it)
        }
    }
}

@Preview
@Composable
fun TrainingContentPreview() {
    TrainingContent(
        TrainingState(
            currentNumber = "123"
        ),
        onRepeatClick = {},
        onSettingsClick = {},
        onBackClick = {}
    )
}