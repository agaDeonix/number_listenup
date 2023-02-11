package com.pinkunicorp.numberlistenup.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pinkunicorp.numberlistenup.ui.screens.Screen
import org.koin.androidx.compose.koinViewModel
import com.pinkunicorp.numberlistenup.R

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
            .background(Color.White)
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(50.dp),
            text = stringResource(id = R.string.home_app_name),
            style = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = stringResource(id = R.string.home_score_title),
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        )
        Text(
            text = state.score.toString(),
            style = TextStyle(
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Column(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { onTrainingNavigation() }
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    text = stringResource(id = R.string.home_start),
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }

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