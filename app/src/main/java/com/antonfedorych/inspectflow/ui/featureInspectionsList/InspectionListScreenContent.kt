package com.antonfedorych.inspectflow.ui.featureInspectionsList

import androidx.compose.foundation.background
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(horizontal = 20.dp)
            .padding(vertical = 24.dp)
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
                            Text(text = item.title.orEmpty(), style = MaterialTheme.typography.titleLarge)
                            Spacer(Modifier.width(8.dp))
                            Text(text = "#" + item.id)
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
                            Text(text = item.title.orEmpty(), style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.width(8.dp))
                            Text(text = "#" + item.id)
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
                            Text(text = "#" + item.id)
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
                                Text(item.imageSrc.toString())
                                Row{
                                    Text(text = item.title.orEmpty(), style = MaterialTheme.typography.labelMedium)
                                    Spacer(Modifier.width(8.dp))
                                    Text(text = "#" + item.id, style = MaterialTheme.typography.labelMedium)
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
                                    Text(text = item.content.orEmpty(), style = MaterialTheme.typography.labelMedium)
                                    Spacer(Modifier.width(8.dp))
                                    Text(text = "#" + item.id, style = MaterialTheme.typography.labelMedium)
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
                                                Text(text = option.label, style = MaterialTheme.typography.labelMedium)
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
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun InspectionListScreenContentPreview() {
    InspectionListScreenContent()
}
