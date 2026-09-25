package com.antonfedorych.inspectflow.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.antonfedorych.inspectflow.data.local.entity.ItemEntity
import com.antonfedorych.inspectflow.data.local.entity.ResponseEntity
import com.antonfedorych.inspectflow.data.local.entity.ResponseSetEntity

data class ResponseSetWithResponses(
    @Embedded val responseSet: ResponseSetEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "response_set_id",
    )
    val responses: List<ResponseEntity>,
)

data class ItemWithResponseSet(
    @Embedded val item: ItemEntity,
    @Relation(
        entity = ResponseSetEntity::class,
        parentColumn = "id",
        entityColumn = "item_id",
    )
    val responseSet: ResponseSetWithResponses?,
)
