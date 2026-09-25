package com.antonfedorych.inspectflow.ui.common.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = InspectPrimary,
    onPrimary = InspectOnSurface,
    secondary = InspectSection,
    onSecondary = InspectOnSecondary,
    background = InspectBackground,
    surface = InspectSurface,
    onBackground = InspectOnSurface,
    onSurface = InspectOnSurface,
    outline = InspectOutline,
)

data class CustomColorScheme(
    val mutedText: Color,
    val chipSelected: Color,
    val badge: Color,
)

private val LightCustomColorScheme = CustomColorScheme(
    mutedText = InspectMutedText,
    chipSelected = InspectChipSelected,
    badge = InspectBadge,
)

val LocalCustomColorScheme = staticCompositionLocalOf<CustomColorScheme> {
    error("No LocalCustomColorScheme defined")
}

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalCustomColorScheme provides LightCustomColorScheme,
        LocalDimens provides Dimens(),
    ) {
        MaterialTheme(
            colorScheme = LightColorScheme,
            typography = Typography,
            content = content
        )
    }
}

object Theme {
    val colors
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colorScheme

    val colorsCustom
        @Composable
        @ReadOnlyComposable
        get() = LocalCustomColorScheme.current

    val typo
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography

    val dimens
        @Composable
        @ReadOnlyComposable
        get() = LocalDimens.current
}
