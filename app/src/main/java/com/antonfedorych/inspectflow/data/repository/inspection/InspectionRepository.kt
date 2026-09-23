package com.antonfedorych.inspectflow.data.repository.inspection

import com.antonfedorych.inspectflow.data.local.InspectionDAO
import com.antonfedorych.inspectflow.data.local.mapper.toEntity
import com.antonfedorych.inspectflow.data.remote.ApiService
import com.antonfedorych.inspectflow.data.remote.mapper.toDomain
import com.antonfedorych.inspectflow.domain.model.Item
import com.antonfedorych.inspectflow.domain.model.state.DataResult
import com.antonfedorych.inspectflow.domain.repository.InspectionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InspectionRepositoryImpl(
    private val apiService: ApiService,
    private val db: InspectionDAO,
) : InspectionRepository {
    override fun observeInspection(): Flow<List<Item>> = flow {
    }

    override fun loadInspection(): Flow<DataResult<List<Item>>> = flow {
        try {
            val result = apiService.loadInspectionList()
            val entityList = result.toEntity()
            db.insertItems(entityList.items)
            db.insertResponseSets(entityList.responseSets)
            db.insertResponses(entityList.responses)

            //emit(DataResult.Success(result))
        } catch (e: Exception) {
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
