package com.antonfedorych.inspectflow.ui.featureInspectionsList.inspectionList

import com.antonfedorych.inspectflow.ui.featureInspectionsList.uimodel.InspectionListRow

data class InspectionListState(
    val items: List<InspectionListRow> = emptyList(),
//    val selectedOptions: Map<Int, Set<Int>> = emptyMap(), //TODO Is it better to use for recomposition performance
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

sealed interface InspectionListEvent {
    data object RefreshItems : InspectionListEvent
    data class ToggleOption(val questionId: Int, val optionId: Int) : InspectionListEvent
    data class OpenImage(val title: String, val imageSrc: String) : InspectionListEvent
}

sealed interface InspectionListEffect {
    data class NavigateToFullscreenImage(val title: String, val imageSrc: String) : InspectionListEffect
    data class ShowError(val message: String) : InspectionListEffect
    data object ShowSuccessRefresh : InspectionListEffect
}
