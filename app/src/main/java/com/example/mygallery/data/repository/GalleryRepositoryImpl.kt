package com.example.mygallery.data.repository

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.mygallery.domain.model.GalleryImage
import com.example.mygallery.domain.repository.GalleryRepository
import kotlinx.coroutines.flow.Flow

class GalleryRepositoryImpl(
    private val context: Context
) : GalleryRepository {

    override fun getImages(): Flow<PagingData<GalleryImage>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GalleryPagingSource(context) }
        ).flow
    }
}
