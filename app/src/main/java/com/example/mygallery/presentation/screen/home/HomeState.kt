package com.example.mygallery.presentation.screen.home

import com.example.mygallery.domain.model.GalleryImage

data class HomeState(
    val images: List<GalleryImage> = emptyList(),
    val isLoading: Boolean = false
)
