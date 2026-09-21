package com.antonfedorych.inspectflow.domain.model

import com.antonfedorych.inspectflow.domain.model.enum.ItemType

data class TextQuestionItem(
    override val id: Int,
    val content: String,
) : Item {
    override val type: ItemType = ItemType.TEXT
}
