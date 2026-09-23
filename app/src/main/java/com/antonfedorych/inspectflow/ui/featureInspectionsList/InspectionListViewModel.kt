package com.antonfedorych.inspectflow.ui.featureInspectionsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antonfedorych.inspectflow.domain.model.state.DataResult
import com.antonfedorych.inspectflow.domain.usecase.inspection.LoadInspectionUseCase
import com.antonfedorych.inspectflow.ui.featureInspectionsList.mapper.toUIModel
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
            if (result is DataResult.Success) {
                _state.update { it.copy(items = result.value.toUIModel()) }
            }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: InspectionListEvent) {
        when (event) {
            is InspectionListEvent.OpenImage -> {}
            is InspectionListEvent.ToggleOption -> {
                _state.update { current ->
                    val item = current.items.find { it.id == event.questionId }
                    val option = item?.choiceOptions?.find { it.id == event.optionId }
                    if (item == null || option == null) return@update current

                    val newOption = option.copy(isSelected = !option.isSelected,)
                    val newItem = item.copy(
                        choiceOptions = item.choiceOptions.map { option ->
                            if (option.id == event.optionId)
                                newOption
                            else if (!item.multipleSelection)
                                option.copy(isSelected = false)
                            else
                                option

                        },
                    )

                    current.copy(
                        items = current.items.map { item ->
                            if (item.id == event.questionId) newItem else item
                        },
                    )
                }
            }
        }
    }
}
