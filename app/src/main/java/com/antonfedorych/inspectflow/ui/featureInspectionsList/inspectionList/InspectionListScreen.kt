package com.antonfedorych.inspectflow.ui.featureInspectionsList.inspectionList

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun InspectionListScreen(
    onNavigateToFullscreenImage: (title: String, imageSrc: String) -> Unit
) {
    val viewmodel = koinViewModel<InspectionListViewModel>()
    val state = viewmodel.state.collectAsState()

    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    var showSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewmodel.effect.collect {
            when (it) {
                is InspectionListEffect.NavigateToFullscreenImage -> onNavigateToFullscreenImage(it.title, it.imageSrc)
                is InspectionListEffect.ShowError -> {
                    showError = true
                    errorMessage = it.message
                }

                InspectionListEffect.ShowSuccessRefresh -> {
                    showSuccess = true
                }
            }
        }
    }

    if (showError) {
        AlertDialog(
            onDismissRequest = { showError = false },
            confirmButton = {
                TextButton(onClick = { showError = false }) {
                    Text("OK")
                }
            },
            title = { Text(errorMessage) },
        )
    }

    if (showSuccess) {
        AlertDialog(
            onDismissRequest = { showSuccess = false },
            confirmButton = {
                TextButton(onClick = { showSuccess = false }) {
                    Text("OK")
                }
            },
            title = { Text("Data refresh successfully") },
        )
    }

    InspectionListScreenContent(
        state = state.value,
        onEvent = viewmodel::onEvent
    )
}
