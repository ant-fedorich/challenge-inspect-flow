package com.antonfedorych.inspectflow.ui.featureInspectionsList

import com.antonfedorych.inspectflow.ui.featureInspectionsList.model.InspectionListRow

data class InspectionsListState(
    val items: List<InspectionListRow> = emptyList(),
    val selectedOptions: Map<Int, Set<Int>> = emptyMap(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

sealed interface InspectionsListEvent {
    data class ToggleOption(
        val questionId: Int,
        val optionId: Int,
    ) : InspectionsListEvent

    data class OpenImage(
        val title: String,
        val imageSrc: String,
    ) : InspectionsListEvent
}

sealed interface InspectionsListEffect {
    data class NavigateToFullscreenImage(
        val title: String,
        val imageSrc: String,
    ) : InspectionsListEffect

    data class ShowError(val message: String) : InspectionsListEffect
}
