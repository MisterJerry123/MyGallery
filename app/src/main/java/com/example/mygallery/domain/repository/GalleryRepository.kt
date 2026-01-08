package com.example.mygallery.domain.repository

import androidx.paging.PagingData
import com.example.mygallery.domain.model.GalleryImage
import kotlinx.coroutines.flow.Flow

interface GalleryRepository {
    fun getImages(): Flow<PagingData<GalleryImage>>
}
