package com.antonfedorych.inspectflow.ui

import androidx.lifecycle.viewmodel.compose.viewModel
import com.antonfedorych.inspectflow.ui.featureInspectionsList.InspectionListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.scope.get
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { InspectionListViewModel(get()) }
}
