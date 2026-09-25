package com.antonfedorych.inspectflow.ui.featureInspectionsList.inspectionList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.antonfedorych.inspectflow.domain.model.enum.ItemType.CHOICE
import com.antonfedorych.inspectflow.domain.model.enum.ItemType.IMAGE
import com.antonfedorych.inspectflow.domain.model.enum.ItemType.PAGE
import com.antonfedorych.inspectflow.domain.model.enum.ItemType.SECTION
import com.antonfedorych.inspectflow.domain.model.enum.ItemType.TEXT
import com.antonfedorych.inspectflow.ui.common.theme.AppTheme
import com.antonfedorych.inspectflow.ui.common.theme.Theme

// TODO: Crash on first loading from api

@Composable
fun InspectionListScreenContent(
    state: InspectionListState = InspectionListState(),
    onEvent: (InspectionListEvent) -> Unit = {}
) {
    PullToRefreshBox(
        isRefreshing = state.isLoading,
        onRefresh = {
            onEvent(InspectionListEvent.RefreshItems)
        },
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colors.background)
            .systemBarsPadding()
            .padding(horizontal = Theme.dimens.screenPaddingHorizontal)
            .padding(top = Theme.dimens.screenPaddingTop, bottom = Theme.dimens.screenPaddingBottom)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
            ,
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(Theme.dimens.inlineSpacing)
            ) {
                items(
                    items = state.items,
                    key = { it.id },
                    contentType = { it.type }
                ) { item ->
                    val selectedOptionIds = state.selectedOptions[item.id].orEmpty()
                    when (item.type) {
                        PAGE -> {
                            Row(
                                Modifier
                                    .padding(start = Theme.dimens.depthInset * item.depth)
                                    .fillMaxWidth()
                                    .padding(Theme.dimens.rowPadding)
                            ) {
                                Text(
                                    text = item.title.orEmpty(),
                                    style = Theme.typo.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Theme.colors.primary
                                )
                                Spacer(Modifier.width(Theme.dimens.inlineSpacing))
                                Text(
                                    text = "#" + item.id,
                                    style = Theme.typo.labelLarge,
                                    color = Theme.colorsCustom.mutedText,
                                )
                            }
                        }
                        SECTION -> {
                            Row(
                                Modifier
                                    .padding(start = Theme.dimens.depthInset * item.depth)
                                    .fillMaxWidth()
                                    .padding(Theme.dimens.rowPaddingSection)
                            ) {
                                Text(
                                    text = item.title.orEmpty(),
                                    style = Theme.typo.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = sectionFontSize(item.depth),
                                    color = Theme.colors.secondary,
                                )
                                Spacer(Modifier.width(Theme.dimens.inlineSpacing))
                                Text(
                                    text = "#" + item.id,
                                    style = Theme.typo.labelLarge,
                                    color = Theme.colorsCustom.mutedText,
                                )
                            }
                        }
                        TEXT -> {
                            Row(
                                Modifier
                                    .padding(start = Theme.dimens.depthInset * item.depth)
                                    .fillMaxWidth()
                                    .padding(Theme.dimens.rowPadding)
                            ) {
                                Text(
                                    text = item.content.orEmpty(),
                                    style = Theme.typo.bodyMedium,
                                    color = Theme.colorsCustom.mutedText,
                                )
                                Spacer(Modifier.width(Theme.dimens.inlineSpacing))
                                Text(
                                    text = "#" + item.id,
                                    style = Theme.typo.labelLarge,
                                    color = Theme.colorsCustom.mutedText,
                                )
                            }
                        }
                        IMAGE -> {
                            Box(
                                Modifier
                                    .padding(start = Theme.dimens.depthInset * item.depth)
                                    .fillMaxWidth()
                                    .padding(Theme.dimens.rowPadding)
                            ){
                                Column {
                                    AsyncImage(
                                        model = item.imageSrc,
                                        contentDescription = item.title,
                                        modifier = Modifier
                                            .width(Theme.dimens.imagePreviewWidth)
                                            .height(Theme.dimens.imagePreviewHeight)
                                            .clickable {
                                                onEvent(
                                                    InspectionListEvent.OpenImage(
                                                        item.title.orEmpty(),
                                                        item.imageSrc.orEmpty(),
                                                    ),
                                                )
                                            },
                                        contentScale = ContentScale.Fit,
                                        placeholder = rememberVectorPainter(image = Icons.Outlined.Image),
                                        error = rememberVectorPainter(Icons.Outlined.ErrorOutline)
                                    )
                                    Row{
                                        Text(
                                            text = item.title.orEmpty(),
                                            style = Theme.typo.labelMedium,
                                            color = Theme.colorsCustom.mutedText,
                                        )
                                        Spacer(Modifier.width(Theme.dimens.inlineSpacing))
                                        Text(
                                            text = "#" + item.id,
                                            style = Theme.typo.labelLarge,
                                            color = Theme.colorsCustom.mutedText,
                                        )
                                    }
                                }
                            }
                        }
                        CHOICE -> {
                            Box(
                                Modifier
                                    .padding(start = Theme.dimens.depthInset * item.depth)
                                    .fillMaxWidth()
                                    .padding(Theme.dimens.rowPadding)
                            ){
                                Column {
                                    Row{
                                        Text(
                                            text = item.content.orEmpty(),
                                            style = Theme.typo.bodyMedium,
                                            color = Theme.colorsCustom.mutedText,
                                        )
                                        Spacer(Modifier.width(Theme.dimens.inlineSpacing))
                                        Text(
                                            text = "#" + item.id,
                                            style = Theme.typo.labelLarge,
                                            color = Theme.colorsCustom.mutedText,
                                        )
                                    }

                                    FlowRow(
                                        horizontalArrangement = Arrangement.spacedBy(Theme.dimens.inlineSpacing)
                                    ) {
                                        item.choiceOptions.forEach { option ->
                                            FilterChip(
                                                selected = option.id in selectedOptionIds,
                                                onClick = {
                                                    onEvent(
                                                        InspectionListEvent.ToggleOption(
                                                            questionId = item.id,
                                                            optionId = option.id
                                                        )
                                                    )
                                                },
                                                colors = FilterChipDefaults.filterChipColors(
                                                    selectedContainerColor = Theme.colorsCustom.chipSelected,
                                                    selectedLabelColor = Theme.colors.onSurface,
                                                ),
                                                label = {
                                                    Text(text = option.label, style = Theme.typo.labelSmall)
                                                },
                                                trailingIcon = {
                                                    option.score?.let {
                                                        Text(
                                                            it.toString()
                                                        )
                                                    }
                                                }
                                            )
                                        }
                                    }
                                    val isSelectedText = if (item.multipleSelection) "multi-select" else "single-select"

                                    val selectedText = if (selectedOptionIds.isEmpty()) "none"
                                    else selectedOptionIds.size.toString()


                                    Text(
                                        text = "$selectedText selected ($isSelectedText)",
                                        style = Theme.typo.labelSmall,
                                        color = Theme.colorsCustom.mutedText,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun sectionFontSize(depth: Int): TextUnit {
    val max = Theme.typo.titleLarge.fontSize.value - 2
    val min = Theme.typo.bodyMedium.fontSize.value + 2
    return (max - 2 * (depth - 1)).coerceIn(min, max).sp
}

@Preview
@Composable
private fun InspectionListScreenContentPreview() {
    AppTheme {
        InspectionListScreenContent()
    }
}
