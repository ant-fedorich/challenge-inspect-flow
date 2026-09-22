package com.antonfedorych.inspectflow.ui.featureInspectionsList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun InspectionListScreenContent(
    state: InspectionListState = InspectionListState(),
    onEvent: (InspectionListEvent) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(horizontal = 20.dp)
            .padding(vertical = 24.dp)
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