package com.antonfedorych.inspectflow.ui.featureInspectionsList.mapper

import com.antonfedorych.inspectflow.app.navigation.Screen
import com.antonfedorych.inspectflow.domain.model.ChoiceQuestionItem
import com.antonfedorych.inspectflow.domain.model.ImageQuestionItem
import com.antonfedorych.inspectflow.domain.model.Item
import com.antonfedorych.inspectflow.domain.model.PageItem
import com.antonfedorych.inspectflow.domain.model.ResponseSet
import com.antonfedorych.inspectflow.domain.model.SectionItem
import com.antonfedorych.inspectflow.domain.model.TextQuestionItem
import com.antonfedorych.inspectflow.ui.featureInspectionsList.uimodel.ChoiceOptionRow
import com.antonfedorych.inspectflow.ui.featureInspectionsList.uimodel.InspectionListRow

fun List<Item>.toUIModel(): List<InspectionListRow> {
    val list = mutableListOf<InspectionListRow>()
    forEach { item ->
        if (item is PageItem) {
            list.add(item.toUIModel())
            if (item.items.isNotEmpty()) list.addAll(item.items.toUIModel())
        } else if (item is SectionItem) {
            list.add(item.toUIModel())
            if (item.items.isNotEmpty()) list.addAll(item.items.toUIModel())
        } else {
            list.add(item.toUIModel())
        }
    }

    return list
}

fun ResponseSet.toUIModel(): List<ChoiceOptionRow> =
    this.responses.map {
        ChoiceOptionRow(
            id = it.id,
            label = it.label
        )
    }

fun Item.toUIModel(): InspectionListRow {
    val item = when (this) {
        is PageItem -> InspectionListRow(
            id = this.id,
            type = this.type,
            depth = 0,
            title = this.title,
        )
        is SectionItem -> InspectionListRow(
            id = this.id,
            type = this.type,
            depth = 0,
            title = this.title,
        )
        is ChoiceQuestionItem -> InspectionListRow(
            id = this.id,
            type = this.type,
            depth = 0,
            content = this.content,
            multipleSelection = this.responseSet.multipleSelection,
            choiceOptions = this.responseSet.toUIModel(),
        )
        is ImageQuestionItem -> InspectionListRow(
            id = this.id,
            type = this.type,
            depth = 0,
            title = this.title,
            imageSrc = this.src,
        )
        is TextQuestionItem -> InspectionListRow(
            id = this.id,
            type = this.type,
            depth = 0,
            content = this.content
        )
    }
    return item
}

