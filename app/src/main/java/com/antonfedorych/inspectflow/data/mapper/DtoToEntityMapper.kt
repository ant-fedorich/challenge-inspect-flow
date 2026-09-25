package com.antonfedorych.inspectflow.data.mapper

import com.antonfedorych.inspectflow.data.local.entity.ItemEntity
import com.antonfedorych.inspectflow.data.local.entity.ResponseEntity
import com.antonfedorych.inspectflow.data.local.entity.ResponseSetEntity
import com.antonfedorych.inspectflow.data.remote.dto.ItemDto
import com.antonfedorych.inspectflow.data.remote.dto.ResponseDto
import com.antonfedorych.inspectflow.data.remote.dto.ResponseSetDto
import kotlin.collections.plusAssign

data class InspectionEntities(
    val items: List<ItemEntity>,
    val responseSets: List<ResponseSetEntity>,
    val responses: List<ResponseEntity>,
)

fun ItemDto.toEntity(parentId: Int?, sortOrder: Int): ItemEntity =
    ItemEntity(
        id = this.id,
        type = this.type,
        parentId = parentId,
        title = this.title,
        content = this.content,
        src = this.src,
        sortOrder = sortOrder,
    )

fun ResponseSetDto.toEntity(itemId: Int): ResponseSetEntity =
    ResponseSetEntity(
        id = this.id,
        itemId = itemId,
        multipleSelection = this.multipleSelection,
    )

fun ResponseDto.toEntity(responseSetId: Int): ResponseEntity =
    ResponseEntity(
        id = this.id,
        responseSetId = responseSetId,
        label = this.label,
        score = this.score,
    )

fun List<ItemDto>.toEntity(): InspectionEntities {
    val items = mutableListOf<ItemEntity>()
    val responseSets = mutableListOf<ResponseSetEntity>()
    val responses = mutableListOf<ResponseEntity>()

    fun flatten(nodes: List<ItemDto>, parentId: Int?) {
        nodes.forEachIndexed { index, node ->
            items += node.toEntity(parentId = parentId, sortOrder = index)
            node.responseSet?.let { set ->
                responseSets += set.toEntity(itemId = node.id)
                responses += set.responses.map { it.toEntity(responseSetId = set.id) }
            }
            flatten(node.items.orEmpty(), parentId = node.id)
        }
    }
    
    flatten(this, parentId = null)
    return InspectionEntities(items, responseSets, responses)
}


