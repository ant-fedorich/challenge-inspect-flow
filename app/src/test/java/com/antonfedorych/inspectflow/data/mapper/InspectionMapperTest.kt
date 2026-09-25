package com.antonfedorych.inspectflow.data.mapper

import com.antonfedorych.inspectflow.data.remote.dto.ItemDto
import com.antonfedorych.inspectflow.domain.model.Item
import com.antonfedorych.inspectflow.domain.model.PageItem
import com.antonfedorych.inspectflow.domain.model.SectionItem
import com.antonfedorych.inspectflow.domain.model.TextQuestionItem
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class InspectionMapperTest {

    @Test
    fun `T1 - deep nesting should keep its shape from DTO to domain`() {
        val dtoTree = listOf(
            ItemDto(id = 0, type = "page", title = "Page", items = listOf(nodeDto(depth = 1))),
        )

        val domainTree = dtoTree.toEntity().toDomain()

        domainTree shouldBe listOf(
            PageItem(id = 0, title = "Page", items = listOf(nodeDomain(depth = 1))),
        )
    }

    private fun nodeDto(depth: Int): ItemDto =
        if (depth == MAX_DEPTH) ItemDto(id = depth, type = "text", content = "Question")
            else ItemDto(id = depth, type = "section", title = "Section", items = listOf(nodeDto(depth + 1)))

    private fun nodeDomain(depth: Int): Item =
        if (depth == MAX_DEPTH) TextQuestionItem(id = depth, content = "Question")
            else SectionItem(id = depth, title = "Section", items = listOf(nodeDomain(depth + 1)))
}

private const val MAX_DEPTH = 10
