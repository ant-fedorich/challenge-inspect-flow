package com.antonfedorych.inspectflow.ui.featureInspectionsList.inspectionList

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.antonfedorych.inspectflow.domain.model.enum.ItemType
import com.antonfedorych.inspectflow.ui.featureInspectionsList.uimodel.InspectionListRow
import io.kotest.matchers.shouldBe
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.test.Test

@RunWith(AndroidJUnit4::class)
class InspectionListScreenContentTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `T8 - tapping an image should send OpenImage with its title and source`() {
        val events = mutableListOf<InspectionListEvent>()
        val state = InspectionListState(
            items = listOf(
                InspectionListRow(
                    id = 1,
                    type = ItemType.IMAGE,
                    depth = 0,
                    title = "Title",
                    imageSrc = "https://image.png",
                ),
            ),
        )
        composeRule.setContent {
            InspectionListScreenContent(state = state, onEvent = { events += it })
        }

        composeRule.onNodeWithContentDescription("Title").performClick()

        events shouldBe listOf(
            InspectionListEvent.OpenImage(title = "Title", imageSrc = "https://image.png"),
        )
    }
}
