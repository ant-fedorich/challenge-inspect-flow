package com.antonfedorych.inspectflow.domain

import com.antonfedorych.inspectflow.domain.usecase.inspection.LoadInspectionUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { LoadInspectionUseCase(get()) }
}
