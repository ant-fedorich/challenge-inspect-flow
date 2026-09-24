package com.antonfedorych.inspectflow.domain.usecase.inspection

import com.antonfedorych.inspectflow.domain.model.state.DataResult
import com.antonfedorych.inspectflow.domain.repository.InspectionRepository
import kotlinx.coroutines.flow.Flow

class RefreshInspectionUseCase(
    private val repo: InspectionRepository,
) {
    operator fun invoke(): Flow<DataResult<Unit>> = repo.refreshInspection()
}
