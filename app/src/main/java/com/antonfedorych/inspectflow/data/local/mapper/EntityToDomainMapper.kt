package com.antonfedorych.inspectflow.data.local.mapper

import com.antonfedorych.inspectflow.data.local.entity.ItemEntity
import com.antonfedorych.inspectflow.data.local.entity.ResponseEntity
import com.antonfedorych.inspectflow.data.local.entity.ResponseSetEntity
import com.antonfedorych.inspectflow.domain.model.ChoiceQuestionItem
import com.antonfedorych.inspectflow.domain.model.ImageQuestionItem
import com.antonfedorych.inspectflow.domain.model.Item
import com.antonfedorych.inspectflow.domain.model.PageItem
import com.antonfedorych.inspectflow.domain.model.Response
import com.antonfedorych.inspectflow.domain.model.ResponseSet
import com.antonfedorych.inspectflow.domain.model.SectionItem
import com.antonfedorych.inspectflow.domain.model.TextQuestionItem

fun InspectionEntities.toDomain(): List<Item> =
    items.toDomainTree(responseSets, responses)

fun List<ItemEntity>.toDomainTree(
    responseSets: List<ResponseSetEntity>,
    responses: List<ResponseEntity>,
): List<Item> {
    fun childrenOf(parentId: Int?): List<ItemEntity> =
        this.filter { it.parentId == parentId }.sortedBy { it.sortOrder }

    fun ItemEntity.toDomain(): Item {
        val childItems = childrenOf(id).map { it.toDomain() }
        return when (this.type.lowercase()) {
            "page" -> PageItem(
                id = id,
                title = requireNotNull(title) { "page item $id requires title" },
                items = childItems,
            )

            "section" -> SectionItem(
                id = id,
                title = requireNotNull(title) { "section item $id requires title" },
                items = childItems,
            )

            "text" -> TextQuestionItem(
                id = id,
                content = requireNotNull(content) { "text item $id requires content" },
            )

            "image" -> ImageQuestionItem(
                id = id,
                title = requireNotNull(title) { "image item $id requires title" },
                src = requireNotNull(src) { "image item $id requires src" },
            )

            "choice" -> {
                val set = responseSets.firstOrNull { it.itemId == id }
                    ?: error("choice item $id requires response_set")
                ChoiceQuestionItem(
                    id = id,
                    content = requireNotNull(content) { "choice item $id requires content" },
                    responseSet = set.toDomain(responses),
                )
            }

            else -> error("Unknown item type '$type' for id $id")
        }
    }

    return childrenOf(parentId = null).map { it.toDomain() }
}

private fun ResponseSetEntity.toDomain(responses: List<ResponseEntity>): ResponseSet =
    ResponseSet(
        id = id,
        multipleSelection = multipleSelection,
        responses = responses
            .filter { it.responseSetId == id }
            .sortedBy { it.id }
            .map { it.toDomain() },
    )

private fun ResponseEntity.toDomain(): Response =
    Response(
        id = id,
        label = label,
        score = score,
    )
