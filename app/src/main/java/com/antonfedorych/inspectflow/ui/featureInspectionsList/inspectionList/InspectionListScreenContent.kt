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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            .systemBarsPadding()
            .padding(horizontal = 20.dp)
            .padding(vertical = 24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
            ,
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = state.items,
                    key = { it.id },
                    contentType = { it.type }
                ) { item ->
                    when (item.type) {
                        PAGE -> {
                            Row(
                                Modifier
                                    .padding(start = 24.dp * item.depth)
                                    .fillMaxWidth()
                                    .background(Color.Gray.copy(0.4f), shape = ShapeDefaults.Medium)
                                    .padding(24.dp)
                            ) {
                                Text(
                                    text = item.title.orEmpty(),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.Red.copy(0.4f)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(text = "#" + item.id, style = MaterialTheme.typography.labelLarge)
                            }
                        }
                        SECTION -> {
                            Row(
                                Modifier
                                    .padding(start = 24.dp * item.depth)
                                    .fillMaxWidth()
                                    .background(Color.Gray.copy(0.4f), shape = ShapeDefaults.Medium)
                                    .padding(24.dp)
                            ) {
                                Text(
                                    text = item.title.orEmpty(),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = sectionFontSize(item.depth),
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(text = "#" + item.id, style = MaterialTheme.typography.labelLarge)
                            }
                        }
                        TEXT -> {
                            Row(
                                Modifier
                                    .padding(start = 24.dp * item.depth)
                                    .fillMaxWidth()
                                    .background(Color.Gray.copy(0.4f), shape = ShapeDefaults.Medium)
                                    .padding(24.dp)
                            ) {
                                Text(text = item.content.orEmpty(), style = MaterialTheme.typography.bodyMedium)
                                Spacer(Modifier.width(8.dp))
                                Text(text = "#" + item.id, style = MaterialTheme.typography.labelLarge)
                            }
                        }
                        IMAGE -> {
                            Box(
                                Modifier
                                    .padding(start = 24.dp * item.depth)
                                    .fillMaxWidth()
                                    .background(Color.Gray.copy(0.4f), shape = ShapeDefaults.Medium)
                                    .padding(24.dp)
                            ){
                                Column {
                                    AsyncImage(
                                        model = item.imageSrc,
                                        contentDescription = null,
                                        modifier = Modifier.clickable {
                                            onEvent(InspectionListEvent.OpenImage(item.title.orEmpty(), item.imageSrc.orEmpty()))
                                        }
                                    )
                                    Row{
                                        Text(text = item.title.orEmpty(), style = MaterialTheme.typography.labelMedium)
                                        Spacer(Modifier.width(8.dp))
                                        Text(text = "#" + item.id, style = MaterialTheme.typography.labelLarge)
                                    }
                                }
                            }
                        }
                        CHOICE -> {
                            Box(
                                Modifier
                                    .padding(start = 24.dp * item.depth)
                                    .fillMaxWidth()
                                    .background(Color.Gray.copy(0.4f), shape = ShapeDefaults.Medium)
                                    .padding(24.dp)
                            ){
                                Column {
                                    Row{
                                        Text(text = item.content.orEmpty(), style = MaterialTheme.typography.bodyMedium)
                                        Spacer(Modifier.width(8.dp))
                                        Text(text = "#" + item.id, style = MaterialTheme.typography.labelLarge)
                                    }

                                    FlowRow(
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        item.choiceOptions.forEach { option ->
                                            FilterChip(
                                                selected = option.isSelected,
                                                onClick = {
                                                    onEvent(
                                                        InspectionListEvent.ToggleOption(
                                                            questionId = item.id,
                                                            optionId = option.id
                                                        )
                                                    )
                                                },
                                                label = {
                                                    Text(text = option.label, style = MaterialTheme.typography.labelSmall)
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

                                    val selectedText = if (item.choiceOptions.all { !it.isSelected }) "none"
                                    else item.choiceOptions.count { it.isSelected }.toString()


                                    Text(text = "$selectedText selected ($isSelectedText)", style = MaterialTheme.typography.labelSmall)
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
    val max = MaterialTheme.typography.titleLarge.fontSize.value - 2
    val min = MaterialTheme.typography.bodyMedium.fontSize.value + 2
    return (max - 2 * (depth - 1)).coerceIn(min, max).sp
}

@Preview
@Composable
private fun InspectionListScreenContentPreview() {
    InspectionListScreenContent()
}
