package de.benkralex.socius.ui.components.editContact

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import de.benkralex.socius.data.model.Contact

class EditProfilePictureState {

    var picture: Bitmap? by mutableStateOf(null)

    fun hasRelevantData(): Boolean {
        return picture != null
    }

    fun loadFromContact(contact: Contact) {
        picture = contact.profilePicture.bitmap
    }

    fun clearPicture() {
        picture = null
    }

    fun reset() {
        picture = null
    }
}