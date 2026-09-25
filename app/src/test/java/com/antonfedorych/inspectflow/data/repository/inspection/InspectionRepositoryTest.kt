package com.antonfedorych.inspectflow.data.repository.inspection

import app.cash.turbine.test
import com.antonfedorych.inspectflow.data.local.InspectionDAO
import com.antonfedorych.inspectflow.data.local.entity.ItemEntity
import com.antonfedorych.inspectflow.data.local.entity.ResponseEntity
import com.antonfedorych.inspectflow.data.local.entity.ResponseSetEntity
import com.antonfedorych.inspectflow.data.mapper.toEntity
import com.antonfedorych.inspectflow.data.remote.ApiService
import com.antonfedorych.inspectflow.data.remote.dto.ItemDto
import com.antonfedorych.inspectflow.domain.model.PageItem
import com.antonfedorych.inspectflow.domain.model.TextQuestionItem
import com.antonfedorych.inspectflow.domain.model.state.DataResult
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class InspectionRepositoryTest {

    private val api = FakeApiService()
    private val dao = FakeInspectionDAO()
    private val repository = InspectionRepositoryImpl(api, dao)

    @Test
    fun `T3 - successful refresh should emit Loading then Success and show the new tree`() = runTest {
        api.response = { listOf(pageDto(questionContent = "Fresh")) }

        repository.refreshInspection().test {
            awaitItem() shouldBe DataResult.Loading
            awaitItem() shouldBe DataResult.Success(Unit)
            awaitComplete()
        }

        repository.observeInspection().first() shouldBe listOf(pageDomain(questionContent = "Fresh"))
    }

    @Test
    fun `T4 - network failure should emit Loading then Failure and keep the cached tree`() = runTest {
        val cached = listOf(pageDto(questionContent = "Cached")).toEntity()
        dao.insertInspectionTransaction(cached.items, cached.responseSets, cached.responses)
        api.response = { throw IOException("No network") }

        repository.refreshInspection().test {
            awaitItem() shouldBe DataResult.Loading
            awaitItem().shouldBeInstanceOf<DataResult.Failure>()
            awaitComplete()
        }

        repository.observeInspection().first() shouldBe listOf(pageDomain(questionContent = "Cached"))
    }

    private fun pageDto(questionContent: String) = ItemDto(
        id = 1,
        type = "page",
        title = "Page",
        items = listOf(ItemDto(id = 2, type = "text", content = questionContent)),
    )

    private fun pageDomain(questionContent: String) = PageItem(
        id = 1,
        title = "Page",
        items = listOf(TextQuestionItem(id = 2, content = questionContent)),
    )
}

private class FakeApiService : ApiService {
    var response: () -> List<ItemDto> = { emptyList() }

    override suspend fun loadInspectionList(): List<ItemDto> = response()
}

// Same as the Room DAO: inserting a row with an existing id replaces it
private class FakeInspectionDAO : InspectionDAO {
    private val items = MutableStateFlow<List<ItemEntity>>(emptyList())
    private val responseSets = MutableStateFlow<List<ResponseSetEntity>>(emptyList())
    private val responses = MutableStateFlow<List<ResponseEntity>>(emptyList())

    override fun getAllItems(): Flow<List<ItemEntity>> = items
    override fun getAllResponseSets(): Flow<List<ResponseSetEntity>> = responseSets
    override fun getAllResponses(): Flow<List<ResponseEntity>> = responses

    override suspend fun insertItems(items: List<ItemEntity>) =
        this.items.update { current -> current.replaceBy(items) { it.id } }

    override suspend fun insertResponseSets(sets: List<ResponseSetEntity>) =
        responseSets.update { current -> current.replaceBy(sets) { it.id } }

    override suspend fun insertResponses(responses: List<ResponseEntity>) =
        this.responses.update { current -> current.replaceBy(responses) { it.id } }

    private fun <T> List<T>.replaceBy(new: List<T>, id: (T) -> Int): List<T> {
        val newIds = new.map(id).toSet()
        return filterNot { id(it) in newIds } + new
    }
}
