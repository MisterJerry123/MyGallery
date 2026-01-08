package com.example.mygallery.presentation.screen.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.mygallery.domain.model.GalleryImage
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeRoot(
    onImageClick: (GalleryImage) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    
    HomeScreen(
        state = state,
        onPermissionGranted = viewModel::onPermissionGranted,
        onImageClick = onImageClick,
        modifier = modifier
    )
}
    