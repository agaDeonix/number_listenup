package com.pinkunicorp.numberlistenup.ui.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import com.pinkunicorp.numberlistenup.R
import com.pinkunicorp.numberlistenup.data.model.NumberType
import com.pinkunicorp.numberlistenup.data.model.NumberVariantState
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    settingsViewModel: SettingsViewModel = koinViewModel()
) {
    val state by settingsViewModel.uiState.collectAsState()

    SettingsContent(
        state = state,
        onBackClick = {
            settingsViewModel.onBackClick()
        }, onNumberVariantChanged = { numberVariant, isEnable ->
            settingsViewModel.onNumberVariantChanged(numberVariant, isEnable)
        }
    )

    state.events.forEach {
        when (it) {
            is SettingsEvent.GoToBack -> navController.navigateUp()
        }
        settingsViewModel.consumeEvents(state.events)
    }
}

@Composable
fun SettingsContent(
    state: SettingsState,
    onNumberVariantChanged: (numberVariant: NumberVariantState, isEnable: Boolean) -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        SettingsToolbarView(
            onBackClick = onBackClick
        )
        LazyColumn {
            item {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    text = stringResource(id = R.string.settings_desc),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
            items(state.numberVariants) { numberVariantState ->
                NumberVariantView(numberVariantState) {
                    onNumberVariantChanged(numberVariantState, it)
                }
            }
        }
    }
}

@Composable
fun NumberVariantView(
    numberVariantState: NumberVariantState,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onCheckedChange(!numberVariantState.isEnable)
            },
        verticalAlignment = Alignment.Top
    ) {
        Checkbox(
            checked = numberVariantState.isEnable, onCheckedChange = {
                onCheckedChange(!numberVariantState.isEnable)
            },
            colors = CheckboxDefaults.colors(
                checkedColor = Color.Black,
            )
        )
        Column(
            modifier = Modifier
                .padding(top = 12.dp)
                .padding(end = 12.dp)
                .fillMaxWidth()

        ) {
            Text(
                text = stringResource(id = getNumberVariantNameId(numberVariantState.type)),
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = stringResource(id = getNumberVariantDescId(numberVariantState.type)),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }
}

private fun getNumberVariantNameId(type: NumberType): Int {
    return when (type) {
        NumberType.WHOLE -> R.string.settings_number_whole_title
        NumberType.ORDINAL -> R.string.settings_number_ordinal_title
        NumberType.RATIONAL -> R.string.settings_number_rational_title
        NumberType.FRACTION -> R.string.settings_number_fraction_title
        NumberType.TIME -> R.string.settings_number_time_title
    }
}

private fun getNumberVariantDescId(type: NumberType): Int {
    return when (type) {
        NumberType.WHOLE -> R.string.settings_number_whole_desc
        NumberType.ORDINAL -> R.string.settings_number_ordinal_desc
        NumberType.RATIONAL -> R.string.settings_number_rational_desc
        NumberType.FRACTION -> R.string.settings_number_fraction_desc
        NumberType.TIME -> R.string.settings_number_time_desc
    }
}

@Composable
fun SettingsToolbarView(
    modifier: Modifier = Modifier,
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
            text = stringResource(id = R.string.settings_title),
            modifier = Modifier.align(Alignment.Center),
            style = TextStyle(
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsContent(state = SettingsState(
        numberVariants = listOf(
            NumberVariantState(
                type = NumberType.WHOLE,
                isEnable = true
            ),
            NumberVariantState(
                type = NumberType.ORDINAL,
                isEnable = false
            ),
        )
    ),
        onBackClick = {},
        onNumberVariantChanged = { _, _ -> }
    )
}