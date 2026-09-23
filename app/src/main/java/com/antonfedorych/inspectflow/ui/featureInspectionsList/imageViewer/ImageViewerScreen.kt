package com.antonfedorych.inspectflow.ui.featureInspectionsList.imageViewer

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable

//Even if ImageViewerScreen is stateless now, it is better to be consistent here. It is still a screen aka destination
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageViewerScreen(
    title: String,
    imageSrc: String,
    onNavigateBack: () -> Unit,
) {
    ImageViewerScreenContent(
        title = title,
        imageSrc = imageSrc,
        onEvent = onNavigateBack,
    )
}
