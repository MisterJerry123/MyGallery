package com.example.mygallery.presentation.screen.imageDetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.mygallery.core.routing.Route
import org.koin.androidx.compose.koinViewModel

@Composable
fun ImageDetailRoot(
    route: Route.ImageDetail,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ImageDetailViewModel = koinViewModel()
) {
    LaunchedEffect(route) {
        viewModel.load(route)
    }

    val state by viewModel.state.collectAsState()

    ImageDetailScreen(
        state = state,
        onBackClick = onBackClick,
        modifier = modifier
    )
}