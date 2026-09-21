package com.antonfedorych.inspectflow.domain.model

import com.antonfedorych.inspectflow.domain.model.enum.ItemType

data class ImageQuestionItem(
    override val id: Int,
    val title: String,
    val src: String,
) : Item {
    override val type: ItemType = ItemType.IMAGE
}
