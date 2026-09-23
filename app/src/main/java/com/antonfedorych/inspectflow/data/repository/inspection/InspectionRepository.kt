package com.antonfedorych.inspectflow.data.repository.inspection

import com.antonfedorych.inspectflow.data.local.InspectionDAO
import com.antonfedorych.inspectflow.data.local.mapper.toDomainTree
import com.antonfedorych.inspectflow.data.local.mapper.toEntity
import com.antonfedorych.inspectflow.data.remote.ApiService
import com.antonfedorych.inspectflow.domain.model.Item
import com.antonfedorych.inspectflow.domain.model.state.DataResult
import com.antonfedorych.inspectflow.domain.repository.InspectionRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.seconds

class InspectionRepositoryImpl(
    private val apiService: ApiService,
    private val dao: InspectionDAO,
) : InspectionRepository {
    override fun observeInspection(): Flow<List<Item>> =
        combine(
            dao.getAllItems(),
            dao.getAllResponseSets(),
            dao.getAllResponses(),
        ) { items, sets, responses ->
            items.toDomainTree(sets, responses)
        }

    override fun syncInspection(): Flow<DataResult<Unit>> = flow {
        emit(DataResult.Loading)
        delay(1.seconds)
        try {
            val result = apiService.loadInspectionList()
            val entityList = result.toEntity()
            dao.insertItems(entityList.items)
            dao.insertResponseSets(entityList.responseSets)
            dao.insertResponses(entityList.responses)
            // TODO: Add @Transaction?

            emit(DataResult.Success(Unit))
        } catch (e: Exception) {
            // TODO: Handle Cancellation?
            emit(DataResult.Failure(e.message.orEmpty()))
        }
    }

    override suspend fun toggleOption(
        questionId: Int,
        optionId: Int,
    ): DataResult<Unit> {
        TODO("Not yet implemented")
    }
}
