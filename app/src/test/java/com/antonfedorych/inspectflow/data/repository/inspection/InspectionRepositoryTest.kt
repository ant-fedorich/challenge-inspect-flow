package com.antonfedorych.inspectflow.data.repository.inspection

import app.cash.turbine.test
import com.antonfedorych.inspectflow.data.local.InspectionDAO
import com.antonfedorych.inspectflow.data.local.entity.ItemEntity
import com.antonfedorych.inspectflow.data.local.entity.ResponseEntity
import com.antonfedorych.inspectflow.data.local.entity.ResponseSetEntity
import com.antonfedorych.inspectflow.data.local.relation.ItemWithResponseSet
import com.antonfedorych.inspectflow.data.mapper.InspectionEntities
import com.antonfedorych.inspectflow.data.mapper.toEntity
import com.antonfedorych.inspectflow.data.mapper.toRows
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

    @Test
    fun `T9 - successful refresh should drop rows missing from the new snapshot`() = runTest {
        val cached = listOf(
            pageDto(questionContent = "Old"),
            ItemDto(id = 99, type = "text", content = "Gone"),
        ).toEntity()
        dao.insertInspectionTransaction(cached.items, cached.responseSets, cached.responses)
        api.response = { listOf(pageDto(questionContent = "Fresh")) }

        repository.refreshInspection().test {
            awaitItem() shouldBe DataResult.Loading
            awaitItem() shouldBe DataResult.Success(Unit)
            awaitComplete()
        }

        repository.observeInspection().first() shouldBe listOf(pageDomain(questionContent = "Fresh"))
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

// 🧹 The transaction clears first, so each insert writes into an empty list.
private class FakeInspectionDAO : InspectionDAO {
    private val items = MutableStateFlow<List<ItemEntity>>(emptyList())
    private val responseSets = MutableStateFlow<List<ResponseSetEntity>>(emptyList())
    private val responses = MutableStateFlow<List<ResponseEntity>>(emptyList())
    private val rows = MutableStateFlow<List<ItemWithResponseSet>>(emptyList())

    override fun observeItems(): Flow<List<ItemWithResponseSet>> = rows

    override suspend fun insertInspectionTransaction(
        items: List<ItemEntity>,
        sets: List<ResponseSetEntity>,
        responses: List<ResponseEntity>,
    ) {
        clearResponses()
        clearResponseSets()
        clearItems()
        insertItems(items)
        insertResponseSets(sets)
        insertResponses(responses)
        rows.value = InspectionEntities(
            this.items.value,
            responseSets.value,
            this.responses.value,
        ).toRows()
    }

    override suspend fun insertItems(items: List<ItemEntity>) =
        this.items.update { current -> current.replaceBy(items) { it.id } }

    override suspend fun insertResponseSets(sets: List<ResponseSetEntity>) =
        responseSets.update { current -> current.replaceBy(sets) { it.id } }

    override suspend fun insertResponses(responses: List<ResponseEntity>) =
        this.responses.update { current -> current.replaceBy(responses) { it.id } }

    override suspend fun clearResponses() {
        responses.value = emptyList()
    }

    override suspend fun clearResponseSets() {
        responseSets.value = emptyList()
    }

    override suspend fun clearItems() {
        items.value = emptyList()
    }

    private fun <T> List<T>.replaceBy(new: List<T>, id: (T) -> Int): List<T> {
        val newIds = new.map(id).toSet()
        return filterNot { id(it) in newIds } + new
    }
}
