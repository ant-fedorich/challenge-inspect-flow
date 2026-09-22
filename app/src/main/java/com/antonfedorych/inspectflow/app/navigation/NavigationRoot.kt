package com.antonfedorych.inspectflow.app.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.antonfedorych.inspectflow.ui.featureInspectionsList.InspectionListScreen
import kotlinx.serialization.Serializable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationRoot(
    paddingValues: PaddingValues
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.InspectionList
    ) {
        composable<Screen.InspectionList> {
            InspectionListScreen {
                navController.navigate(Screen.ImagePreviewer)
            }
        }

        composable<Screen.ImagePreviewer> {
            BasicAlertDialog(
                onDismissRequest = {}
            ) {
                Text("Hello World")
            }
        }
    }
}

@Serializable
object Screen {
    @Serializable
    object InspectionList
    @Serializable
    object ImagePreviewer
}
