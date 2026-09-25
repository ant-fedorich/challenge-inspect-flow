package com.antonfedorych.inspectflow.ui.featureInspectionsList.inspectionList

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.antonfedorych.inspectflow.domain.model.enum.ItemType.CHOICE
import com.antonfedorych.inspectflow.domain.model.enum.ItemType.IMAGE
import com.antonfedorych.inspectflow.domain.model.enum.ItemType.PAGE
import com.antonfedorych.inspectflow.domain.model.enum.ItemType.SECTION
import com.antonfedorych.inspectflow.domain.model.enum.ItemType.TEXT
import com.antonfedorych.inspectflow.ui.common.theme.AppTheme
import com.antonfedorych.inspectflow.ui.common.theme.Theme
import com.antonfedorych.inspectflow.ui.featureInspectionsList.uimodel.ChoiceOptionRow

@Composable
fun InspectionListScreenContent(
    state: InspectionListState = InspectionListState(),
    onEvent: (InspectionListEvent) -> Unit = {},
    scaffoldPadding: PaddingValues = PaddingValues(),
) {
    PullToRefreshBox(
        isRefreshing = state.isLoading,
        onRefresh = {
            onEvent(InspectionListEvent.RefreshItems)
        },
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colors.background)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = Theme.dimens.screenPaddingHorizontal,
                    end = Theme.dimens.screenPaddingHorizontal,
                    top = scaffoldPadding.calculateTopPadding() + Theme.dimens.screenPaddingTop,
                    bottom = scaffoldPadding.calculateBottomPadding() + Theme.dimens.screenPaddingBottom,
                ),
            ) {
                itemsIndexed(
                    items = state.items,
                    key = { _, item -> item.id },
                    contentType = { _, item -> item.type }
                ) { index, item ->
                    val selectedOptionIds = state.selectedOptions[item.id].orEmpty()
                    PageBlock(
                        showTopSpacing = index > 0 && item.type == PAGE,
                        depth = item.depth,
                    ) {
                    when (item.type) {
                        PAGE -> {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(Theme.dimens.rowPadding),
                            ) {
                                Text(
                                    text = item.title.orEmpty(),
                                    modifier = Modifier.alignByBaseline(),
                                    style = Theme.typo.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Theme.colors.primary
                                )
                                Spacer(Modifier.width(Theme.dimens.inlineSpacing))
                                IdBadge(item.id)
                            }
                        }
                        SECTION -> {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(Theme.dimens.rowPaddingSection),
                            ) {
                                Text(
                                    text = item.title.orEmpty(),
                                    modifier = Modifier.alignByBaseline(),
                                    style = Theme.typo.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = sectionFontSize(item.depth),
                                    color = Theme.colors.secondary,
                                )
                                Spacer(Modifier.width(Theme.dimens.inlineSpacing))
                                IdBadge(item.id)
                            }
                        }
                        TEXT -> {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(Theme.dimens.rowPadding),
                            ) {
                                Text(
                                    text = item.content.orEmpty(),
                                    modifier = Modifier.alignByBaseline(),
                                    style = Theme.typo.bodyMedium,
                                    color = Theme.colorsCustom.mutedText,
                                )
                                Spacer(Modifier.width(Theme.dimens.inlineSpacing))
                                IdBadge(item.id)
                            }
                        }
                        IMAGE -> {
                            Box(
                                Modifier
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
                                            .clip(RoundedCornerShape(8.dp))
                                            .border(1.dp, Theme.colors.outline, RoundedCornerShape(8.dp))
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
                                    Row {
                                        Text(
                                            text = item.title.orEmpty(),
                                            modifier = Modifier.alignByBaseline(),
                                            style = Theme.typo.labelMedium,
                                            color = Theme.colorsCustom.mutedText,
                                        )
                                        Spacer(Modifier.width(Theme.dimens.inlineSpacing))
                                        IdBadge(item.id)
                                    }
                                }
                            }
                        }
                        CHOICE -> {
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(Theme.dimens.rowPadding)
                            ){
                                Column {
                                    Row {
                                        Text(
                                            text = item.content.orEmpty(),
                                            modifier = Modifier.alignByBaseline(),
                                            style = Theme.typo.bodyMedium,
                                            color = Theme.colorsCustom.mutedText,
                                        )
                                        Spacer(Modifier.width(Theme.dimens.inlineSpacing))
                                        IdBadge(item.id)
                                    }

                                    ChoiceChips(
                                        options = item.choiceOptions,
                                        selectedOptionIds = selectedOptionIds,
                                        onToggleOption = { optionId ->
                                            onEvent(InspectionListEvent.ToggleOption(item.id, optionId))
                                        },
                                    )
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
}

@Composable
private fun ChoiceChips(
    options: List<ChoiceOptionRow>,
    selectedOptionIds: Set<Int>,
    onToggleOption: (optionId: Int) -> Unit,
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(Theme.dimens.inlineSpacing)
    ) {
        options.forEach { option ->
            FilterChip(
                selected = option.id in selectedOptionIds,
                onClick = { onToggleOption(option.id) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Theme.colorsCustom.chipSelected,
                    selectedLabelColor = Theme.colors.onSurface,
                ),
                label = {
                    Text(text = option.label, style = Theme.typo.labelSmall)
                },
                trailingIcon = {
                    option.score?.let { ScoreBadge(it) }
                },
            )
        }
    }
}

@Composable
private fun PageBlock(
    showTopSpacing: Boolean,
    depth: Int,
    content: @Composable () -> Unit,
) {
    Column(Modifier.fillMaxWidth()) {
        if (showTopSpacing) {
            Spacer(Modifier.height(Theme.dimens.pageSpacing))
        }
        val barColor = Theme.colors.primary
        val barWidth = Theme.dimens.pageBarWidth
        Box(
            Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawRect(
                        color = barColor,
                        size = Size(barWidth.toPx(), size.height),
                    )
                }
                .padding(
                    start = Theme.dimens.pageBarWidth +
                        Theme.dimens.contentStart +
                        Theme.dimens.depthInset * depth,
                    bottom = Theme.dimens.inlineSpacing,
                )
        ) {
            content()
        }
    }
}

@Composable
private fun RowScope.IdBadge(id: Int) {
    Text(
        text = "#$id",
        style = Theme.typo.labelSmall,
        color = Theme.colorsCustom.mutedText,
        modifier = Modifier
            .alignByBaseline()
            .background(Theme.colorsCustom.badge, RoundedCornerShape(50))
            .padding(
                horizontal = Theme.dimens.badgePaddingHorizontal,
                vertical = Theme.dimens.badgePaddingVertical,
            ),
    )
}

@Composable
private fun ScoreBadge(score: Int) {
    Box(
        modifier = Modifier
            .size(Theme.dimens.scoreBadgeSize)
            .background(Theme.colorsCustom.badge, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = score.toString(),
            style = Theme.typo.labelSmall,
            color = Theme.colorsCustom.mutedText,
        )
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
