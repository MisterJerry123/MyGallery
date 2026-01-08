package com.example.mygallery.presentation.screen.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.mygallery.domain.model.GalleryImage
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeRoot(
    onImageClick: (GalleryImage) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val pagingItems = state.pagedImages.collectAsLazyPagingItems()
    
    Scaffold(
        modifier = modifier
    ) { innerPadding ->
        HomeScreen(
            pagingItems = pagingItems,
            onPermissionGranted = viewModel::onPermissionGranted,
            onImageClick = onImageClick,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
    