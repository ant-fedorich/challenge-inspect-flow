package com.antonfedorych.inspectflow.domain.usecase.inspection

import com.antonfedorych.inspectflow.domain.model.Item
import com.antonfedorych.inspectflow.domain.repository.InspectionRepository
import kotlinx.coroutines.flow.Flow

class ObserveInspectionUseCase(
    private val repo: InspectionRepository
) {
    operator fun invoke(): Flow<List<Item>> = repo.observeInspection()
}