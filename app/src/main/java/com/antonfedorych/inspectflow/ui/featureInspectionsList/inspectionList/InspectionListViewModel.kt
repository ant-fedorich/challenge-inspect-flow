package com.antonfedorych.inspectflow.ui.featureInspectionsList.inspectionList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antonfedorych.inspectflow.domain.model.state.DataResult
import com.antonfedorych.inspectflow.domain.usecase.inspection.ObserveInspectionUseCase
import com.antonfedorych.inspectflow.domain.usecase.inspection.RefreshInspectionUseCase
import com.antonfedorych.inspectflow.ui.featureInspectionsList.mapper.toUIModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InspectionListViewModel(
    private val observeInspectionUseCase: ObserveInspectionUseCase,
    private val refreshInspectionUseCase: RefreshInspectionUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(InspectionListState())
    val state = _state.asStateFlow()

    private val _effect = Channel<InspectionListEffect>(capacity = 1)
    val effect = _effect.receiveAsFlow()

    init {
        refreshInspectionUseCase().onEach { result ->
            when(result) {
                is DataResult.Loading -> _state.update { it.copy(isLoading = true) }
                is DataResult.Failure -> {
                    _state.update { it.copy(isLoading = false) }
                    _effect.send(InspectionListEffect.ShowError(result.message))
                }
                is DataResult.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    _effect.send(InspectionListEffect.ShowSuccessRefresh)
                }
            }

        }.launchIn(viewModelScope)

        observeInspectionUseCase().onEach { list ->
            _state.update { it.copy(items = list.toUIModel()) }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: InspectionListEvent) {
        when (event) {
            is InspectionListEvent.OpenImage -> {
                viewModelScope.launch {
                    _effect.send(
                        InspectionListEffect.NavigateToFullscreenImage(
                            title = event.title,
                            imageSrc = event.imageSrc,
                        ),
                    )
                }
            }
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
