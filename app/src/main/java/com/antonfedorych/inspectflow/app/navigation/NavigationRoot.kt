package com.antonfedorych.inspectflow.app.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.antonfedorych.inspectflow.ui.featureInspectionsList.imageViewer.ImageViewerScreen
import com.antonfedorych.inspectflow.ui.featureInspectionsList.inspectionList.InspectionListScreen
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
                InspectionListScreen { title, src ->
                    navController.navigate(Screen.ImageViewer(title, src))
                }
            }

            composable<Screen.ImageViewer> {
                val args = it.toRoute<Screen.ImageViewer>()
                ImageViewerScreen(
                    title = args.title,
                    imageSrc = args.imageSrc,
                    onNavigateBack = { navController.popBackStack() },
                )
            }
    }
}

@Serializable
object Screen {
    @Serializable
    data object InspectionList
    @Serializable
    data class ImageViewer(val title: String, val imageSrc: String)
}
