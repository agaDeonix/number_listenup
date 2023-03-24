package com.pinkunicorp.numberlistenup.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pinkunicorp.numberlistenup.R
import com.pinkunicorp.numberlistenup.ui.elements.NumbersKeyboardButton
import com.pinkunicorp.numberlistenup.ui.screens.Screen
import com.pinkunicorp.numberlistenup.ui.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = koinViewModel()
) {
    val state by homeViewModel.uiState.collectAsState()
    HomeContent(state) {
        homeViewModel.onTrainingClick()
    }
    state.events.forEach {
        when (it) {
            is HomeEvent.GoToTraining -> navController.navigate(Screen.Training.route)
        }
        homeViewModel.consumeEvents(state.events)
    }
}

@Composable
fun HomeContent(state: HomeState, onTrainingNavigation: () -> Unit) {
    Column(
        modifier = Modifier
            .background(AppTheme.colors.background)
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(50.dp),
            text = stringResource(id = R.string.home_app_name),
            style = AppTheme.typography.header,
            textAlign = TextAlign.Center,
            color = AppTheme.colors.text
        )
        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = stringResource(id = R.string.home_score_title),
            style = AppTheme.typography.subHeader,
            color = AppTheme.colors.text
        )
        Text(
            text = state.score.toString(),
            style = AppTheme.typography.score,
            color = AppTheme.colors.text
        )
        Column(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
        ) {
            NumbersKeyboardButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                content = {
                    val color = if (it) AppTheme.colors.main else AppTheme.colors.mainContent
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 40.dp)
                            .padding(vertical = 10.dp),
                        text = stringResource(id = R.string.home_start),
                        color = color,
                        style = AppTheme.typography.text
                    )
                },
                onClick = { onTrainingNavigation() }
            )
        }
    }
}

@Preview
@Composable
fun HomeContentPreview() {
    HomeContent(
        state = HomeState(
            score = 0
        ),
        onTrainingNavigation = {}
    )
}