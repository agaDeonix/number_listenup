package com.pinkunicorp.numberlistenup.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.pinkunicorp.numberlistenup.ui.theme.AppTheme

@Composable
fun NumberFieldView(
    modifier: Modifier = Modifier,
    currentNumber: String,
    isCorrect: Boolean,
    hasError: Boolean,
) {
    val textColor = if (isCorrect) AppTheme.colors.mainContent else if (hasError) AppTheme.colors.main else AppTheme.colors.mainContent
    val backgroundColor = if (isCorrect) AppTheme.colors.main else if (hasError) AppTheme.colors.mainContent else AppTheme.colors.main
    val borderWidth = if (isCorrect || hasError) 3.dp else 1.dp
    Box(
        modifier = modifier
            .border(width = borderWidth, color = textColor, shape = AppTheme.shapes.large)
            .background(backgroundColor, shape = AppTheme.shapes.large)
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(vertical = 12.dp),
            text = currentNumber,
            style = AppTheme.typography.textField.copy(
                color = textColor
            )
        )
    }
}

@Preview
@Composable
fun NumberFieldViewPreview() {
    NumberFieldView(
        modifier = Modifier
            .background(AppTheme.colors.background)
            .padding(8.dp),
        currentNumber = "123456789",
        isCorrect = false,
        hasError = false
    )
}

@Preview
@Composable
fun NumberFieldViewWithCorrectPreview() {
    NumberFieldView(
        modifier = Modifier
            .background(AppTheme.colors.background)
            .padding(8.dp),
        currentNumber = "123456789",
        hasError = false,
        isCorrect = true
    )
}

@Preview
@Composable
fun NumberFieldViewWithErrorPreview() {
    NumberFieldView(
        modifier = Modifier
            .background(AppTheme.colors.background)
            .padding(8.dp),
        currentNumber = "123456789",
        hasError = true,
        isCorrect = false
    )
}