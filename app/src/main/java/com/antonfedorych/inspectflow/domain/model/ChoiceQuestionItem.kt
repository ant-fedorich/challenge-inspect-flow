package com.antonfedorych.inspectflow.domain.model

import com.antonfedorych.inspectflow.domain.model.enum.ItemType

data class ChoiceQuestionItem(
    override val id: Int,
    val content: String,
    val responseSet: ResponseSet,
) : Item {
    override val type: ItemType = ItemType.CHOICE
}
