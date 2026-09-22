package com.antonfedorych.inspectflow.data

import com.antonfedorych.inspectflow.data.repository.inspection.InspectionRepositoryImpl
import com.antonfedorych.inspectflow.domain.repository.InspectionRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<InspectionRepository> { InspectionRepositoryImpl(get()) }
}
