package com.antonfedorych.inspectflow.data.remote.mapper

import com.antonfedorych.inspectflow.data.remote.dto.ItemDto
import com.antonfedorych.inspectflow.data.remote.dto.ResponseDto
import com.antonfedorych.inspectflow.data.remote.dto.ResponseSetDto
import com.antonfedorych.inspectflow.domain.model.ChoiceQuestionItem
import com.antonfedorych.inspectflow.domain.model.ImageQuestionItem
import com.antonfedorych.inspectflow.domain.model.Item
import com.antonfedorych.inspectflow.domain.model.PageItem
import com.antonfedorych.inspectflow.domain.model.Response
import com.antonfedorych.inspectflow.domain.model.ResponseSet
import com.antonfedorych.inspectflow.domain.model.SectionItem
import com.antonfedorych.inspectflow.domain.model.TextQuestionItem

fun ItemDto.toDomain(): Item =
    when (type.lowercase()) {
        "page" -> PageItem(
            id = id,
            title = requireNotNull(title) { "page item $id requires title" },
            items = items.orEmpty().map { it.toDomain() },
        )

        "section" -> SectionItem(
            id = id,
            title = requireNotNull(title) { "section item $id requires title" },
            items = items.orEmpty().map { it.toDomain() },
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

        "choice" -> ChoiceQuestionItem(
            id = id,
            content = requireNotNull(content) { "choice item $id requires content" },
            responseSet = requireNotNull(responseSet) { "choice item $id requires response_set" }
                .toDomain(),
        )

        else -> throw IllegalArgumentException("Unknown item type '$type' for id $id")
    }

fun ResponseSetDto.toDomain(): ResponseSet =
    ResponseSet(
        id = id,
        multipleSelection = multipleSelection,
        responses = responses.map { it.toDomain() },
    )

fun ResponseDto.toDomain(): Response =
    Response(
        id = id,
        label = label,
        score = score,
    )
