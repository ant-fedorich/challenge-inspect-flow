package com.antonfedorych.inspectflow.ui.common.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object BaseDimens {
    val dp1: Dp = 1.dp
    val dp3: Dp = 3.dp
    val dp4: Dp = 4.dp
    val dp6: Dp = 6.dp
    val dp8: Dp = 8.dp
    val dp16: Dp = 16.dp
    val dp18: Dp = 18.dp
    val dp20: Dp = 20.dp
    val dp24: Dp = 24.dp
    val dp150: Dp = 150.dp
    val dp200: Dp = 200.dp
}

@Immutable
data class Dimens(
    val screenPaddingHorizontal: Dp = BaseDimens.dp16,
    val screenPaddingTop: Dp = BaseDimens.dp8,
    val screenPaddingBottom: Dp = BaseDimens.dp24,
    val depthInset: Dp = BaseDimens.dp20,
    val contentStart: Dp = BaseDimens.dp16,
    val pageBarWidth: Dp = BaseDimens.dp3,
    val pageSpacing: Dp = BaseDimens.dp20,
    val rowPadding: Dp = BaseDimens.dp6,
    val rowPaddingSection: Dp = BaseDimens.dp8,
    val inlineSpacing: Dp = BaseDimens.dp8,
    val inlineSpacingSmall: Dp = BaseDimens.dp4,
    val badgePaddingHorizontal: Dp = BaseDimens.dp4,
    val badgePaddingVertical: Dp = BaseDimens.dp1,
    val imagePreviewWidth: Dp = BaseDimens.dp200,
    val imagePreviewHeight: Dp = BaseDimens.dp150,
    val scoreBadgeSize: Dp = BaseDimens.dp18,
)

val LocalDimens = staticCompositionLocalOf { Dimens() }
