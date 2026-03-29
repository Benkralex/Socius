package de.benkralex.socius.data.model

import android.graphics.Bitmap
import kotlinx.serialization.Serializable

@Serializable
data class ProfilePicture(
    @Serializable(with = BitmapSerializer::class)
    var bitmap: Bitmap? = null,
)
