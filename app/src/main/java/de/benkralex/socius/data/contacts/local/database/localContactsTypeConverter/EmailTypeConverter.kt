package de.benkralex.socius.data.contacts.local.database.localContactsTypeConverter

import androidx.room.TypeConverter
import de.benkralex.socius.data.model.Email
import kotlinx.serialization.json.Json

class EmailTypeConverter {
    private val json = Json { prettyPrint = true }

    @TypeConverter
    fun fromEmail(value: List<Email>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toEmail(string: String): List<Email>? {
        return try {
            json.decodeFromString<List<Email>>(string)
        } catch (e: Exception) {
            null
        }
    }
}