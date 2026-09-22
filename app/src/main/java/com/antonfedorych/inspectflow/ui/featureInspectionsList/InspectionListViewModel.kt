package com.antonfedorych.inspectflow.ui.featureInspectionsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antonfedorych.inspectflow.domain.model.state.DataResult
import com.antonfedorych.inspectflow.domain.usecase.inspection.LoadInspectionUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

class InspectionListViewModel(
    private val loadInspectionUseCase: LoadInspectionUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(InspectionListState())
    val state = _state.asStateFlow()

    private val _effect = Channel<InspectionListEffect>(capacity = 1)
    val effect = _effect.receiveAsFlow()

    init {
        loadInspectionUseCase().onEach { result ->
            if (result is DataResult.Success)
                _effect.send(InspectionListEffect.ShowError(result.toString()))
//                _state.update { it.copy(errorMessage = result.toString()) }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: InspectionListEvent) {
        when (event) {
            is InspectionListEvent.OpenImage -> {}
            is InspectionListEvent.ToggleOption -> {}
        }
    }
}