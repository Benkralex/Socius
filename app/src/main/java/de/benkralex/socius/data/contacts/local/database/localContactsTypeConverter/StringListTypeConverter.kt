package de.benkralex.socius.data.contacts.local.database.localContactsTypeConverter

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class StringListTypeConverter {
    private val json = Json { prettyPrint = true }

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toStringList(string: String): List<String>? {
        return try {
            json.decodeFromString<List<String>>(string)
        } catch (e: Exception) {
            null
        }
    }
}