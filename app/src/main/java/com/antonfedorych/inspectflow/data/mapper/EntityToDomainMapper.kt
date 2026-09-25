package com.antonfedorych.inspectflow.data.mapper

import com.antonfedorych.inspectflow.data.local.entity.ResponseEntity
import com.antonfedorych.inspectflow.data.local.relation.ItemWithResponseSet
import com.antonfedorych.inspectflow.data.local.relation.ResponseSetWithResponses
import com.antonfedorych.inspectflow.domain.model.ChoiceQuestionItem
import com.antonfedorych.inspectflow.domain.model.ImageQuestionItem
import com.antonfedorych.inspectflow.domain.model.Item
import com.antonfedorych.inspectflow.domain.model.PageItem
import com.antonfedorych.inspectflow.domain.model.Response
import com.antonfedorych.inspectflow.domain.model.ResponseSet
import com.antonfedorych.inspectflow.domain.model.SectionItem
import com.antonfedorych.inspectflow.domain.model.TextQuestionItem

fun InspectionEntities.toDomain(): List<Item> = toRows().toDomainTree()

fun InspectionEntities.toRows(): List<ItemWithResponseSet> =
    items.map { item ->
        val set = responseSets.find { it.itemId == item.id }
        ItemWithResponseSet(
            item = item,
            responseSet = set?.let { entity ->
                ResponseSetWithResponses(
                    responseSet = entity,
                    responses = responses
                        .filter { it.responseSetId == entity.id }
                        .sortedBy { it.id },
                )
            },
        )
    }

fun List<ItemWithResponseSet>.toDomainTree(): List<Item> {
    fun childrenOf(parentId: Int?): List<ItemWithResponseSet> =
        filter { it.item.parentId == parentId }.sortedBy { it.item.sortOrder }

    fun ItemWithResponseSet.toDomain(): Item {
        val childItems = childrenOf(item.id).map { it.toDomain() }
        return when (item.type.lowercase()) {
            "page" -> PageItem(
                id = item.id,
                title = requireNotNull(item.title) { "page item ${item.id} requires title" },
                items = childItems,
            )

            "section" -> SectionItem(
                id = item.id,
                title = requireNotNull(item.title) { "section item ${item.id} requires title" },
                items = childItems,
            )

            "text" -> TextQuestionItem(
                id = item.id,
                content = requireNotNull(item.content) { "text item ${item.id} requires content" },
            )

            "image" -> ImageQuestionItem(
                id = item.id,
                title = requireNotNull(item.title) { "image item ${item.id} requires title" },
                src = requireNotNull(item.src) { "image item ${item.id} requires src" },
            )

            "choice" -> {
                val set = responseSet ?: error("choice item ${item.id} requires response_set")
                ChoiceQuestionItem(
                    id = item.id,
                    content = requireNotNull(item.content) { "choice item ${item.id} requires content" },
                    responseSet = set.toDomain(),
                )
            }

            else -> error("Unknown item type '${item.type}' for id ${item.id}")
        }
    }

    return childrenOf(parentId = null).map { it.toDomain() }
}

private fun ResponseSetWithResponses.toDomain(): ResponseSet =
    ResponseSet(
        id = responseSet.id,
        multipleSelection = responseSet.multipleSelection,
        responses = responses.sortedBy { it.id }.map { it.toDomain() },
    )

private fun ResponseEntity.toDomain(): Response =
    Response(
        id = id,
        label = label,
        score = score,
    )
