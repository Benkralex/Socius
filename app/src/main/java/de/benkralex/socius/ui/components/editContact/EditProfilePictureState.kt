package de.benkralex.socius.ui.components.editContact

import android.graphics.Bitmap
import android.net.Uri
import androidx.core.net.toUri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import de.benkralex.socius.data.model.Contact

class EditProfilePictureState {

    var picture: Bitmap? by mutableStateOf(null)
    var pictureUri: Uri? by mutableStateOf(null)

    fun hasRelevantData(): Boolean {
        return picture != null || pictureUri != null
    }

    fun loadFromContact(contact: Contact) {
        picture = contact.photoBitmap
        pictureUri = contact.photoUri?.toUri()
    }

    fun setPicture(uri: Uri, bitmap: Bitmap?) {
        pictureUri = uri
        picture = bitmap
    }

    fun clearPicture() {
        picture = null
        pictureUri = null
    }

    fun reset() {
        picture = null
        pictureUri = null
    }
}