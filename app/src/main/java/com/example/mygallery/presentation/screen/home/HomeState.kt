package com.example.mygallery.presentation.screen.home

import androidx.paging.PagingData
import com.example.mygallery.domain.model.GalleryImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class HomeState(
    val pagedImages: Flow<PagingData<GalleryImage>> = emptyFlow(),
    val isLoading: Boolean = false // Kept for other loading states if needed, though Paging handles its own
)
