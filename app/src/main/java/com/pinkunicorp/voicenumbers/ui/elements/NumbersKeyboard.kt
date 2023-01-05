package com.pinkunicorp.voicenumbers.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

sealed class Key {
    data class Number(val value: Int) : Key()
    object Backspace : Key()
    object Slash : Key()
    object Done : Key()
    object Dot : Key()
}

@Composable
fun NumbersKeyboard(modifier: Modifier = Modifier, onNumberClick: (Key) -> Unit) {
    Row(modifier = modifier
        .padding(bottom = 4.dp)
        .padding(horizontal = 4.dp)
        .fillMaxWidth()
        .height(IntrinsicSize.Min)
    ) {
        Column(
            modifier = Modifier
                .weight(3f)
        ) {
            (1..3).forEach { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    (1..3).forEach { column ->
                        val number = row * 3 - 3 + column
                        NumbersKeyboardTextButton(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(4.dp),
                            text = number.toString(),
                            onClick = { onNumberClick(Key.Number(number)) }
                        )
                    }
                }
            }
            Row {
                NumbersKeyboardTextButton(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .padding(4.dp),
                    text = "/",
                    onClick = { onNumberClick(Key.Slash) }
                )
                NumbersKeyboardTextButton(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .padding(4.dp),
                    text = "0",
                    onClick = { onNumberClick(Key.Number(0)) }
                )
                NumbersKeyboardTextButton(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .padding(4.dp),
                    text = ".",
                    onClick = { onNumberClick(Key.Dot) }
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            NumbersKeyboardTextButton(
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(4.dp),
                text = "<",
                onClick = { onNumberClick(Key.Backspace) }
            )
            NumbersKeyboardTextButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(4.dp),
                text = "Ok",
                onClick = { onNumberClick(Key.Done) }
            )
        }
    }
}

@Composable
fun NumbersKeyboardTextButton(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .clickable { onClick() }
            .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = TextStyle(
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Preview
@Composable
fun NumbersKeyboardsPreview() {
    NumbersKeyboard(
        modifier = Modifier.background(Color.White),
        onNumberClick = {}
    )
}