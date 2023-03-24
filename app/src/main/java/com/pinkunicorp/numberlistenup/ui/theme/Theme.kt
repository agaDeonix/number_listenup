package com.pinkunicorp.numberlistenup.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

val LocalNumberListenUpColor: ProvidableCompositionLocal<NumberListenUpColorsScheme> =
    staticCompositionLocalOf { error("no provided") }

val LocalNumberListenUpTypography: ProvidableCompositionLocal<NumberListenUpTypography> =
    staticCompositionLocalOf { error("no provided") }

val LocalNumberListenUpShape: ProvidableCompositionLocal<NumberListenUpShape> =
    staticCompositionLocalOf { error("no provided") }

object AppTheme {
    val colors: NumberListenUpColorsScheme
        @Composable
        get() = LocalNumberListenUpColor.current
    val typography: NumberListenUpTypography
        @Composable
        get() = LocalNumberListenUpTypography.current
    val shapes: NumberListenUpShape
        @Composable
        get() = LocalNumberListenUpShape.current
}

fun mapMaterialColorScheme(
    darkTheme: Boolean,
    colors: NumberListenUpColorsScheme
) = if (darkTheme) {
    darkColors(
        primary = colors.main,
        onPrimary = colors.mainContent,
        background = colors.background,
        onBackground = colors.text,
        secondary = colors.background,
        onSecondary = colors.text
    )
} else {
    lightColors(
        primary = colors.main,
        onPrimary = colors.mainContent,
        background = colors.background,
        onBackground = colors.text,
        secondary = colors.background,
        onSecondary = colors.text
    )
}

@Composable
fun AppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val localNumberListenUpColors: NumberListenUpColorsScheme = if (darkTheme) {
        NumberListenUpDarkColors
    } else {
        NumberListenUpLightColors
    }

    CompositionLocalProvider(
        LocalNumberListenUpColor provides localNumberListenUpColors,
        LocalNumberListenUpTypography provides NumberListenUpTypography,
        LocalNumberListenUpShape provides NumberListenUpShape
    ) {
        MaterialTheme(
            colors = mapMaterialColorScheme(darkTheme, localNumberListenUpColors)
        ) {
            content()
        }
    }
}