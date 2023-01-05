package com.pinkunicorp.voicenumbers.ui.screens.score

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pinkunicorp.voicenumbers.R

data class ScoreState(
    val bestScore: Int,
    val currentScore: Int
)

@Composable
fun ScoreScreen(
    state: ScoreState
) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.score_best_score_title),
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        )
        Text(
            text = state.bestScore.toString(),
            style = TextStyle(
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(id = R.string.score_current_score_title),
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        )
        Text(
            text = state.currentScore.toString(),
            style = TextStyle(
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Column(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .padding(top = 20.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { /*TODO*/ }
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    text = stringResource(id = R.string.score_try_again),
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { /*TODO*/ }
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    text = stringResource(id = R.string.score_finish),
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
fun ScoreScreenPreview() {
    ScoreScreen(
        ScoreState(
            bestScore = 100,
            currentScore = 10
        )
    )
}