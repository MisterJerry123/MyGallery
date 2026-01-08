package com.example.mygallery.presentation.screen.home

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.mygallery.domain.model.GalleryImage

@Composable
fun HomeScreen(
    pagingItems: LazyPagingItems<GalleryImage>,
    onPermissionGranted: () -> Unit,
    onImageClick: (GalleryImage) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onPermissionGranted()
            pagingItems.refresh()
        }
    }

    LaunchedEffect(Unit) {
        launcher.launch(permission)
    }

    Box(modifier = modifier.fillMaxSize()) {
        val refreshState = pagingItems.loadState.refresh

        when {
            refreshState is LoadState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            refreshState is LoadState.Error -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Permission needed or error occurred")
                    Button(onClick = { launcher.launch(permission) }) {
                        Text("Grant Permission")
                    }
                }
            }
            pagingItems.itemCount == 0 -> {
                Text("No images found", modifier = Modifier.align(Alignment.Center))
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(pagingItems.itemCount) { index ->
                        val image = pagingItems[index]
                        if (image != null) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(image.contentUri)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = image.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(120.dp)
                                    .clickable { onImageClick(image) }
                            )
                        }
                    }
                }
            }
        }
    }
}