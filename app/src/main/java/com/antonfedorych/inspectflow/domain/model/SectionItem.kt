package com.antonfedorych.inspectflow.domain.model

import com.antonfedorych.inspectflow.domain.model.enum.ItemType

data class SectionItem(
    override val id: Int,
    val title: String,
    val items: List<Item>,
) : Item {
    override val type: ItemType = ItemType.SECTION
}
