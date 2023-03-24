package com.pinkunicorp.numberlistenup.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pinkunicorp.numberlistenup.R
import com.pinkunicorp.numberlistenup.ui.theme.AppTheme

@Composable
fun RepeatListeningButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    NumbersKeyboardButton(
        modifier = modifier
            .fillMaxHeight()
            .aspectRatio(1f),
        content = {
            val color = if (it) AppTheme.colors.main else AppTheme.colors.mainContent
            Image(
                modifier = Modifier
                    .padding(24.dp)
                    .height(54.dp)
                    .width(54.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_repeat_listening),
                contentDescription = "",
                colorFilter = ColorFilter.tint(color)
            )
        },
        onClick = onClick
    )
}

@Preview
@Composable
fun RepeatListeningButtonPreview() {
    RepeatListeningButton(
        modifier = Modifier.background(AppTheme.colors.background),
        onClick = {}
    )
}