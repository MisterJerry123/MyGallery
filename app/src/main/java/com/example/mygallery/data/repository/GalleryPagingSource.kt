package com.example.mygallery.data.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mygallery.domain.model.GalleryImage

class GalleryPagingSource(
    private val context: Context
) : PagingSource<Int, GalleryImage>() {

    override fun getRefreshKey(state: PagingState<Int, GalleryImage>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(state.config.pageSize)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(state.config.pageSize)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GalleryImage> {
        val offset = params.key ?: 0
        val limit = params.loadSize

        val images = mutableListOf<GalleryImage>()
        
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_TAKEN
        )

        try {
            val cursor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // API 26+ use Bundle for pagination
                val queryArgs = Bundle().apply {
                    putInt(ContentResolver.QUERY_ARG_OFFSET, offset)
                    putInt(ContentResolver.QUERY_ARG_LIMIT, limit)
                    putStringArray(
                        ContentResolver.QUERY_ARG_SORT_COLUMNS,
                        arrayOf(MediaStore.Images.Media.DATE_TAKEN)
                    )
                    putInt(
                        ContentResolver.QUERY_ARG_SORT_DIRECTION,
                        ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
                    )
                }
                context.contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    queryArgs,
                    null
                )
            } else {
                // Fallback for older APIs (using SQL injection in sortOrder which is common for MediaStore)
                val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC LIMIT $limit OFFSET $offset"
                context.contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    null,
                    null,
                    sortOrder
                )
            }

            cursor?.use {
                val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val nameColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val dateTakenColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)

                while (it.moveToNext()) {
                    val id = it.getLong(idColumn)
                    val name = it.getString(nameColumn)
                    val dateTaken = it.getLong(dateTakenColumn)
                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    )
                    images.add(GalleryImage(id, contentUri, name, dateTaken))
                }
            }

            val nextKey = if (images.size < limit) null else offset + limit
            val prevKey = if (offset == 0) null else offset - limit

            return LoadResult.Page(
                data = images,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}
