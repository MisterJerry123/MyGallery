package com.example.mygallery.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mygallery.domain.model.GalleryImage
import com.example.mygallery.domain.repository.GalleryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel(
    private val repository: GalleryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()
    val pagedImages: Flow<PagingData<GalleryImage>> = repository.getImages()
        .cachedIn(viewModelScope)

    fun onPermissionGranted() {
        // No-op: Flow is already active. UI calls pagingItems.refresh()
    }
}