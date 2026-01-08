package com.example.mygallery.presentation.screen.imageDetail

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.mygallery.core.routing.Route
import com.example.mygallery.domain.model.GalleryImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ImageDetailViewModel : ViewModel() {

    private val _state = MutableStateFlow(ImageDetailState())
    val state: StateFlow<ImageDetailState> = _state.asStateFlow()

    fun load(route: Route.ImageDetail) {
        val image = GalleryImage(
            id = route.id,
            contentUri = Uri.parse(route.uri),
            name = route.name,
            dateTaken = route.dateTaken
        )
        _state.update { it.copy(image = image) }
    }
}