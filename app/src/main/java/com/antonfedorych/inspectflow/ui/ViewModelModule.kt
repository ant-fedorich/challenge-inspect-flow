package com.antonfedorych.inspectflow.ui

import com.antonfedorych.inspectflow.ui.featureInspectionsList.inspectionList.InspectionListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { InspectionListViewModel(get()) }
}
