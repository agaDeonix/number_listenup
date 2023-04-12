package com.pinkunicorp.numberlistenup.ui.screens.training

import android.widget.Toast
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
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
import com.pinkunicorp.numberlistenup.R
import com.pinkunicorp.numberlistenup.other.tts.TTSService
import com.pinkunicorp.numberlistenup.ui.elements.*
import com.pinkunicorp.numberlistenup.ui.screens.Screen
import com.pinkunicorp.numberlistenup.ui.theme.AppTheme
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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
            .background(AppTheme.colors.background)
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
                contentDescription = "",
                colorFilter = ColorFilter.tint(AppTheme.colors.mainContent)
            )
        }

        Text(
            text = stringResource(id = R.string.training_title),
            modifier = Modifier.align(Alignment.Center),
            style = AppTheme.typography.textBold,
            color = AppTheme.colors.mainContent,
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
                contentDescription = "",
                colorFilter = ColorFilter.tint(AppTheme.colors.mainContent)
            )
        }
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
            .background(AppTheme.colors.background)
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        TrainingToolbarView(
            onSettingsClick = onSettingsClick,
            onBackClick = onBackClick
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            RepeatListeningButton(
                modifier = Modifier
                    .height(125.dp)
                    .align(Alignment.Center),
                onClick = {
                    onRepeatClick()
                })
            NumbersKeyboardButton(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterEnd),
                backgroundColor = AppTheme.colors.mainContent,
                contentColor = AppTheme.colors.background,
                content = {
                    val color = if (it) AppTheme.colors.mainContent else AppTheme.colors.background
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(top = 4.dp, bottom = 6.dp)
                            .padding(horizontal = 12.dp),
                        text = stringResource(id = R.string.training_answer),
                        style = AppTheme.typography.smallTextBold.copy(
                            color = color
                        )
                    )
                },
                onClick = { onAnswerClick() }
            )
        }
        Box {
            NumberFieldView(
                modifier = Modifier.padding(8.dp),
                currentNumber = state.currentNumber,
                hasError = state.fieldState == TrainingFieldState.Error,
                isCorrect = state.fieldState is TrainingFieldState.Correct
                        || state.fieldState is TrainingFieldState.Answer
            )
            if (state.fieldState is TrainingFieldState.Correct) {
                val color = AppTheme.colors.mainContent
                val animColor = remember { Animatable(color) }
                val yPos = remember { Animatable(0f) }
                LaunchedEffect(true) {
                    awaitAll(
                        async { animColor.animateTo(color.copy(alpha = 0f), tween(1000)) },
                        async { yPos.animateTo(-40f, tween(1000)) }
                    )
                }
                Text(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .align(Alignment.CenterEnd)
                        .padding(top = 4.dp, bottom = 6.dp)
                        .padding(horizontal = 12.dp)
                        .absoluteOffset(y = yPos.value.dp),
                    text = "+${state.fieldState.prize}",
                    style = TextStyle(
                        color = animColor.value,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            if (state.fieldState is TrainingFieldState.Error) {
                val color = AppTheme.colors.main
                val animColor = remember { Animatable(color) }
                val yPos = remember { Animatable(0f) }
                LaunchedEffect(true) {
                    awaitAll(
                        async { animColor.animateTo(color.copy(alpha = 0f), tween(1000)) },
                        async { yPos.animateTo(-40f, tween(1000)) }
                    )
                }
                Image(
                    modifier = Modifier
                        .width(60.dp)
                        .height(60.dp)
                        .padding(bottom = 8.dp)
                        .align(Alignment.CenterEnd)
                        .padding(top = 4.dp, bottom = 6.dp)
                        .padding(horizontal = 12.dp)
                        .absoluteOffset(y = yPos.value.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_error),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(animColor.value)
                )
            }
        }

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