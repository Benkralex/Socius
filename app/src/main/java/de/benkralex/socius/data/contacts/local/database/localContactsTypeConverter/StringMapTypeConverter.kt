package de.benkralex.socius.data.contacts.local.database.localContactsTypeConverter

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class StringMapTypeConverter {
    private val json = Json { prettyPrint = true }

    @TypeConverter
    fun fromStringMap(value: Map<String, String>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toStringMap(string: String): Map<String, String>? {
        return try {
            json.decodeFromString<Map<String, String>>(string)
        } catch (_: Exception) {
            null
        }
    }
}