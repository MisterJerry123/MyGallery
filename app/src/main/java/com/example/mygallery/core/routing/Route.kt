package com.example.mygallery.core.routing

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Route : NavKey {
    @Serializable
    data object Home : Route

    @Serializable
    data class ImageDetail(
        val id: Long,
        val uri: String,
        val name: String,
        val dateTaken: Long?
    ) : Route
}
