package com.antonfedorych.inspectflow.domain.repository

import com.antonfedorych.inspectflow.domain.model.state.DataResult
import com.antonfedorych.inspectflow.domain.model.Item
import kotlinx.coroutines.flow.Flow

interface InspectionRepository {
    fun observeInspection(): Flow<List<Item>>
    fun refreshInspection(): Flow<DataResult<Unit>>
    suspend fun toggleOption(questionId: Int, optionId: Int): DataResult<Unit>
}
