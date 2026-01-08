package com.example.mygallery.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygallery.domain.repository.GalleryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: GalleryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        loadImages()
    }

    fun onPermissionGranted() {
        loadImages()
    }

    private fun loadImages() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            repository.getImages().collectLatest { images ->
                _state.update {
                    it.copy(
                        images = images,
                        isLoading = false
                    )
                }
            }
        }
    }
}