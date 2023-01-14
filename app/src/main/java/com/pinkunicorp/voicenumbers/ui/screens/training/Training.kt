package com.pinkunicorp.voicenumbers.ui.screens.training

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        onAnswerClick = {
            trainingViewModel.onAnswerClick()
        }
    )
    state.events.forEach {
        when (it) {
            is TrainingEvent.GoToBack -> navController.navigateUp()
            is TrainingEvent.GoToSettings -> navController.navigate(Screen.Settings.route)
            is TrainingEvent.PlayNumber -> ttsService.speak(it.number)
            TrainingEvent.NoneTypesForTrainings -> {
                ShowErrorNoneSelectedTypes()
            }
        }
        trainingViewModel.consumeEvents(state.events)
    }
}

@Composable
fun ShowErrorNoneSelectedTypes() {
    Toast.makeText(
        LocalContext.current,
        R.string.settings_error_none_selected_types,
        Toast.LENGTH_SHORT
    ).show()
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
    onAnswerClick: () -> Unit,
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
            Text(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterEnd)
                    .background(
                        color = Color.Blue,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { onAnswerClick() }
                    .padding(top = 4.dp, bottom = 6.dp)
                    .padding(horizontal = 12.dp),
                text = stringResource(id = R.string.training_answer),
                style = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
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
        onAnswerClick = {},
        onBackClick = {}
    )
}