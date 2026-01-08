package com.example.mygallery.domain.model

import android.net.Uri

data class GalleryImage(
    val id: Long,
    val contentUri: Uri,
    val name: String,
    val dateTaken: Long?
)
