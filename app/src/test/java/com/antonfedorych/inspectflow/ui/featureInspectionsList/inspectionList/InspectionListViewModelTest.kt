package com.antonfedorych.inspectflow.ui.featureInspectionsList.inspectionList

import app.cash.turbine.test
import com.antonfedorych.inspectflow.domain.model.ChoiceQuestionItem
import com.antonfedorych.inspectflow.domain.model.Item
import com.antonfedorych.inspectflow.domain.model.Response
import com.antonfedorych.inspectflow.domain.model.ResponseSet
import com.antonfedorych.inspectflow.domain.model.state.DataResult
import com.antonfedorych.inspectflow.domain.repository.InspectionRepository
import com.antonfedorych.inspectflow.domain.usecase.inspection.ObserveInspectionUseCase
import com.antonfedorych.inspectflow.domain.usecase.inspection.RefreshInspectionUseCase
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class InspectionListViewModelTest {

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `T5 - single selection should replace the previous option`() {
        val viewModel = viewModelWith(choiceQuestion(multipleSelection = false))
        viewModel.onEvent(InspectionListEvent.ToggleOption(questionId = QUESTION_ID, optionId = 1))

        viewModel.onEvent(InspectionListEvent.ToggleOption(questionId = QUESTION_ID, optionId = 2))

        viewModel.state.value.selectedOptions[QUESTION_ID] shouldBe setOf(2)
    }

    @Test
    fun `T6 - multiple selection should add the tapped option`() {
        val viewModel = viewModelWith(choiceQuestion(multipleSelection = true))
        viewModel.onEvent(InspectionListEvent.ToggleOption(questionId = QUESTION_ID, optionId = 1))

        viewModel.onEvent(InspectionListEvent.ToggleOption(questionId = QUESTION_ID, optionId = 2))

        viewModel.state.value.selectedOptions[QUESTION_ID] shouldBe setOf(1, 2)
    }

    @Test
    fun `T7 - opening an image should emit exactly one navigation effect`() = runTest {
        val viewModel = viewModelWith()

        viewModel.effect.test {
            viewModel.onEvent(InspectionListEvent.OpenImage(title = "Title", imageSrc = "https://image.png"))

            awaitItem() shouldBe InspectionListEffect.NavigateToFullscreenImage(
                title = "Title",
                imageSrc = "https://image.png",
            )
            expectNoEvents()
        }
    }

    private fun viewModelWith(vararg items: Item): InspectionListViewModel {
        val repository = FakeInspectionRepository(items.toList())
        return InspectionListViewModel(
            observeInspectionUseCase = ObserveInspectionUseCase(repository),
            refreshInspectionUseCase = RefreshInspectionUseCase(repository),
        )
    }

    private fun choiceQuestion(multipleSelection: Boolean) = ChoiceQuestionItem(
        id = QUESTION_ID,
        content = "Question",
        responseSet = ResponseSet(
            id = 100,
            multipleSelection = multipleSelection,
            responses = listOf(
                Response(id = 1, label = "Yes", score = null),
                Response(id = 2, label = "No", score = null),
            ),
        ),
    )

    private companion object {
        const val QUESTION_ID = 10
    }
}

// Refresh does nothing here, so it can't send extra effects into the tests
private class FakeInspectionRepository(private val items: List<Item>) : InspectionRepository {
    override fun observeInspection(): Flow<List<Item>> = flowOf(items)
    override fun refreshInspection(): Flow<DataResult<Unit>> = emptyFlow()
    override suspend fun toggleOption(questionId: Int, optionId: Int): DataResult<Unit> =
        DataResult.Success(Unit)
}
