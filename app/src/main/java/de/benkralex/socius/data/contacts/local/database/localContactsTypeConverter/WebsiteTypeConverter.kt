package de.benkralex.socius.data.contacts.local.database.localContactsTypeConverter

import androidx.room.TypeConverter
import de.benkralex.socius.data.model.Website
import kotlinx.serialization.json.Json

class WebsiteTypeConverter {
    private val json = Json { prettyPrint = true }

    @TypeConverter
    fun fromWebsite(value: List<Website>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toWebsite(string: String): List<Website>? {
        return try {
            json.decodeFromString<List<Website>>(string)
        } catch (_: Exception) {
            null
        }
    }
}