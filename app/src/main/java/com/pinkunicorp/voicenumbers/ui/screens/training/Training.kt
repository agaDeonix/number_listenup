package com.pinkunicorp.voicenumbers.ui.screens.training

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.pinkunicorp.voicenumbers.R
import com.pinkunicorp.voicenumbers.ui.elements.Key
import com.pinkunicorp.voicenumbers.ui.elements.NumberFieldView
import com.pinkunicorp.voicenumbers.ui.elements.NumbersKeyboard
import com.pinkunicorp.voicenumbers.ui.elements.RepeatListeningButton
import net.gotev.speech.Speech


@Composable
fun TrainingScreen(
    navController: NavController,
    trainingViewModel: TrainingViewModel = viewModel()
) {
    val state by trainingViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val exoPlayer by remember { mutableStateOf(ExoPlayer.Builder(context).build()) }

    TrainingContent(
        state = state,
        onRepeatClick = {
            trainingViewModel.onRepeatClick()
        },
        onBackClick = {
            exoPlayer.release()
            trainingViewModel.onBackClick()
        },
        onNumberKeyClick = { number ->
            trainingViewModel.onNumberKeyClick(number)
        },
    )
    state.events.forEach {
        when (it) {
            is TrainingEvent.GoToBack -> navController.navigateUp()
            is TrainingEvent.PlayNumber -> {
                exoPlayer.stop()
                playNumber(exoPlayer, it.number)
            }
        }
        trainingViewModel.consumeEvents(state.events)
    }
}

private fun playNumber(exoPlayer: ExoPlayer, number: Long) {
    Speech.getInstance().say(number.toString())
    val listMediaPlayer = mutableListOf<MediaItem?>()
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

//    exoPlayer.setMediaItems(listMediaPlayer.filterNotNull())
//    exoPlayer.setPlaybackSpeed(1.5F)
//    exoPlayer.prepare()
//    exoPlayer.play()
}

fun getMediaItem(number: String): MediaItem? {
    return when (number) {
        "0" -> getSound(R.raw.number_0)
        "1" -> getSound(R.raw.number_1)
        "2" -> getSound(R.raw.number_2)
        "3" -> getSound(R.raw.number_3)
        "4" -> getSound(R.raw.number_4)
        "5" -> getSound(R.raw.number_5)
        "6" -> getSound(R.raw.number_6)
        "7" -> getSound(R.raw.number_7)
        "8" -> getSound(R.raw.number_8)
        "9" -> getSound(R.raw.number_9)
        "10" -> getSound(R.raw.number_10)
        "11" -> getSound(R.raw.number_11)
        "12" -> getSound(R.raw.number_12)
        "13" -> getSound(R.raw.number_13)
        "14" -> getSound(R.raw.number_14)
        "15" -> getSound(R.raw.number_15)
        "16" -> getSound(R.raw.number_16)
        "17" -> getSound(R.raw.number_17)
        "18" -> getSound(R.raw.number_18)
        "19" -> getSound(R.raw.number_19)
        "20" -> getSound(R.raw.number_20)
        "30" -> getSound(R.raw.number_30)
        "40" -> getSound(R.raw.number_40)
        "50" -> getSound(R.raw.number_50)
        "60" -> getSound(R.raw.number_60)
        "70" -> getSound(R.raw.number_70)
        "80" -> getSound(R.raw.number_80)
        "90" -> getSound(R.raw.number_90)
        "hundred" -> getSound(R.raw.hundred)
        "thousand" -> getSound(R.raw.thousand)
        "million" -> getSound(R.raw.million)
        "billion" -> getSound(R.raw.billion)
        "dot" -> getSound(R.raw.dot)
        else -> null
    }
}

private fun getSound(sound: Int) = MediaItem.fromUri(RawResourceDataSource.buildRawResourceUri(sound))

@Composable
fun TrainingToolbarView(modifier: Modifier = Modifier, onBackClick: () -> Unit) {
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
        TrainingToolbarView(onBackClick = {
            onBackClick()
        })
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
        onBackClick = {}
    )
}