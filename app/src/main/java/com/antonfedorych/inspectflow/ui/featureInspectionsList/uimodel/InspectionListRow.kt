package com.antonfedorych.inspectflow.ui.featureInspectionsList.uimodel

import com.antonfedorych.inspectflow.domain.model.enum.ItemType

data class ChoiceOptionRow(
    val id: Int,
    val label: String,
    val score: Int? = null,
    val isSelected: Boolean = false
)

data class InspectionListRow(
    val id: Int,
    val type: ItemType,
    val depth: Int,
    val title: String? = null,
    val content: String? = null,
    val imageSrc: String? = null,
    val multipleSelection: Boolean = false,
    val choiceOptions: List<ChoiceOptionRow> = emptyList(),
)
