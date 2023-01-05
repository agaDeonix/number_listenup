package com.pinkunicorp.voicenumbers.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pinkunicorp.voicenumbers.R

@Composable
fun RepeatListeningButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .padding(24.dp)
                .height(54.dp)
                .width(54.dp),
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_repeat_listening),
            contentDescription = ""
        )
    }
}

@Preview
@Composable
fun RepeatListeningButtonPreview() {
    RepeatListeningButton(
        modifier = Modifier.background(Color.White),
        onClick = {}
    )
}