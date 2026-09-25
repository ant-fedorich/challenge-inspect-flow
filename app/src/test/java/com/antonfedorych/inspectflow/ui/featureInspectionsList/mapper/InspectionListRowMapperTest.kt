package com.antonfedorych.inspectflow.ui.featureInspectionsList.mapper

import com.antonfedorych.inspectflow.domain.model.PageItem
import com.antonfedorych.inspectflow.domain.model.SectionItem
import com.antonfedorych.inspectflow.domain.model.TextQuestionItem
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class InspectionListRowMapperTest {

    @Test
    fun `T2 - flattened rows should carry depth per level`() {
        val tree = listOf(
            PageItem(
                id = 1,
                title = "Page",
                items = listOf(
                    SectionItem(
                        id = 2,
                        title = "Section",
                        items = listOf(
                            SectionItem(
                                id = 3,
                                title = "Nested section",
                                items = listOf(TextQuestionItem(id = 4, content = "Question")),
                            ),
                        ),
                    ),
                ),
            ),
        )

        val rows = tree.toUIModel()

        rows.map { it.id to it.depth } shouldBe listOf(1 to 0, 2 to 1, 3 to 2, 4 to 3)
    }
}
