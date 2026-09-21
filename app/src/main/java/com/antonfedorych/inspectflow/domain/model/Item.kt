package com.antonfedorych.inspectflow.domain.model

import com.antonfedorych.inspectflow.domain.model.enum.ItemType

sealed interface Item {
    val id: Int
    val type: ItemType
}