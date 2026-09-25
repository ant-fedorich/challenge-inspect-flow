package com.antonfedorych.inspectflow.ui.featureInspectionsList.inspectionList

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.antonfedorych.inspectflow.ui.common.components.ErrorAlertDialog
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun InspectionListScreen(
    onNavigateToFullscreenImage: (title: String, imageSrc: String) -> Unit
) {
    val viewmodel = koinViewModel<InspectionListViewModel>()
    val state = viewmodel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewmodel.effect.collect {
            when (it) {
                is InspectionListEffect.NavigateToFullscreenImage ->
                    onNavigateToFullscreenImage(it.title, it.imageSrc)

                is InspectionListEffect.ShowError -> {
                    showError = true
                    errorMessage = it.message
                }

                InspectionListEffect.ShowSuccessRefresh -> {
                    snackbarHostState.showSnackbar(
                        message = "Data updated!",
                        actionLabel = "OK",
                    )
                }
            }
        }
    }

    if (showError) {
        ErrorAlertDialog(
            title = errorMessage,
            onDismiss = { showError = false },
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        InspectionListScreenContent(
            state = state.value,
            onEvent = viewmodel::onEvent,
            scaffoldPadding = padding,
        )
    }
}
