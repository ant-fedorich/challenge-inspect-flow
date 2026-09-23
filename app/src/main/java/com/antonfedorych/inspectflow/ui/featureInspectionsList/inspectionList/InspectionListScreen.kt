package com.antonfedorych.inspectflow.ui.featureInspectionsList.inspectionList

import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InspectionListScreen(
    onNavigateToFullscreenImage: (title: String, imageSrc: String) -> Unit
) {
    val viewmodel = koinViewModel<InspectionListViewModel>()
    val state = viewmodel.state.collectAsState()

    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewmodel.effect.collect {
            when (it) {
                is InspectionListEffect.NavigateToFullscreenImage -> onNavigateToFullscreenImage(it.title, it.imageSrc)
                is InspectionListEffect.ShowError -> {
                    showError = true
                    errorMessage = it.message
                }
            }
        }
    }

    if (showError) {
        BasicAlertDialog(
            onDismissRequest = { showError = false }
        ) {
            Text(errorMessage)
        }
    }
    InspectionListScreenContent(
        state = state.value,
        onEvent = viewmodel::onEvent
    )
}
