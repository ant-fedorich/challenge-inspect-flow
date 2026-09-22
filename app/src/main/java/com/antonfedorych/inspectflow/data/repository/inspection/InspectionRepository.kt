package com.antonfedorych.inspectflow.data.repository.inspection

import com.antonfedorych.inspectflow.data.remote.ApiService
import com.antonfedorych.inspectflow.data.remote.mapper.toDomain
import com.antonfedorych.inspectflow.domain.model.Item
import com.antonfedorych.inspectflow.domain.model.state.DataResult
import com.antonfedorych.inspectflow.domain.repository.InspectionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InspectionRepositoryImpl(
    private val apiService: ApiService
) : InspectionRepository {
    override fun observeInspection(): Flow<List<Item>> {
        TODO("Not yet implemented")
    }

    override fun loadInspection(): Flow<DataResult<List<Item>>> = flow {
        try {
            val result = apiService.loadInspectionList().map { it.toDomain() }
            emit(DataResult.Success(result))
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
