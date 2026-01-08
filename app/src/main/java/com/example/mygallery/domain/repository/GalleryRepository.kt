package com.example.mygallery.domain.repository

import com.example.mygallery.domain.model.GalleryImage
import kotlinx.coroutines.flow.Flow

interface GalleryRepository {
    fun getImages(): Flow<List<GalleryImage>>
}
