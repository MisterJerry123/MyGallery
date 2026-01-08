package com.example.mygallery.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.mygallery.domain.repository.GalleryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel(
    private val repository: GalleryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(
        HomeState(
            pagedImages = repository.getImages().cachedIn(viewModelScope)
        )
    )
    val state: StateFlow<HomeState> = _state.asStateFlow()

    fun onPermissionGranted() {
        // UI handles refresh
    }
}