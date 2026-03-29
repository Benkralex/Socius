package de.benkralex.socius.data.contacts.local.database.localContactsTypeConverter

import androidx.room.TypeConverter
import de.benkralex.socius.data.model.Phone
import kotlinx.serialization.json.Json

class PhoneTypeConverter {
    private val json = Json { prettyPrint = true }

    @TypeConverter
    fun fromPhone(value: List<Phone>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toPhone(string: String): List<Phone>? {
        return try {
            json.decodeFromString<List<Phone>>(string)
        } catch (e: Exception) {
            null
        }
    }
}