package com.antonfedorych.inspectflow.ui.featureInspectionsList

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun InspectionListScreenContent(
    state: InspectionListState = InspectionListState(),
    onEvent: (InspectionListEvent) -> Unit = {}
) {

}

@Preview
@Composable
private fun InspectionListScreenContentPreview() {
    InspectionListScreenContent()
}