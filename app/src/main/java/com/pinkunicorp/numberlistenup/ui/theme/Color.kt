package com.pinkunicorp.numberlistenup.ui.theme

import androidx.compose.ui.graphics.Color

object NumberListenUpPalette {
    val Black = Color(0xFF000000)
    val White = Color(0xFFFFFFFF)

    val Green80 = Color(0xFF83F9BF)
    val Green40 = Color(0xFF006C47)
    val Green20 = Color(0xFF003823)
    val Gray99 = Color(0xFFFBFDF8)
    val Gray90 = Color(0xFFE1E3DF)
    val Gray10 = Color(0xFF191C1A)
}

interface NumberListenUpColorsScheme {
    val main: Color
    val mainContent: Color
    val background: Color
    val text: Color
}

object NumberListenUpLightColors : NumberListenUpColorsScheme {
    override val main = NumberListenUpPalette.White
    override val mainContent = NumberListenUpPalette.Black
    override val background = NumberListenUpPalette.White
    override val text = NumberListenUpPalette.Black
}

object NumberListenUpDarkColors : NumberListenUpColorsScheme {
    override val main = NumberListenUpPalette.Black
    override val mainContent = NumberListenUpPalette.White
    override val background = NumberListenUpPalette.Black
    override val text = NumberListenUpPalette.White
}
