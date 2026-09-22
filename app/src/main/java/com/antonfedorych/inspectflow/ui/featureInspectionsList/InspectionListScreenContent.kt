package com.antonfedorych.inspectflow.ui.featureInspectionsList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun InspectionListScreenContent(
    state: InspectionListState = InspectionListState(),
    onEvent: (InspectionListEvent) -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = state.items.toString()
        )
    }
}

@Preview
@Composable
private fun InspectionListScreenContentPreview() {
    InspectionListScreenContent()
}