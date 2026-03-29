package de.benkralex.socius.data.contacts.local.database.localContactsTypeConverter

import androidx.room.TypeConverter
import de.benkralex.socius.data.model.ProfilePicture
import kotlinx.serialization.json.Json

class ProfilePictureTypeConverter {
    private val json = Json { prettyPrint = true }

    @TypeConverter
    fun fromProfilePicture(value: ProfilePicture): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toProfilePicture(string: String): ProfilePicture? {
        return try {
            json.decodeFromString<ProfilePicture>(string)
        } catch (_: Exception) {
            null
        }
    }
}