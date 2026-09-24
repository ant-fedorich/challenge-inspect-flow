package com.antonfedorych.inspectflow.domain

import com.antonfedorych.inspectflow.domain.usecase.inspection.ObserveInspectionUseCase
import com.antonfedorych.inspectflow.domain.usecase.inspection.RefreshInspectionUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { ObserveInspectionUseCase(get()) }
    single { RefreshInspectionUseCase(get()) }
}
