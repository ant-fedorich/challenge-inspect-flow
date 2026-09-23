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
                    val ite = current.items.find { it.id == event.questionId }
                    val op = ite?.choiceOptions?.find { it.id == event.optionId }
                    if (ite == null || op == null) return@update current

                    val newOp = op.copy(
                        id = op.id,
                        label = op.label,
                        score = op.score,
                        isSelected = !op.isSelected,
                    )
                    val newIte = ite.copy(
                        id = ite.id,
                        type = ite.type,
                        depth = ite.depth,
                        title = ite.title,
                        content = ite.content,
                        imageSrc = ite.imageSrc,
                        multipleSelection = ite.multipleSelection,
                        choiceOptions = ite.choiceOptions.map { option ->
                            if (option.id == event.optionId) newOp else option
                        },
                    )

                    current.copy(
                        items = current.items.map { item ->
                            if (item.id == event.questionId) newIte else item
                        },
                    )
                }
            }
        }
    }
}
