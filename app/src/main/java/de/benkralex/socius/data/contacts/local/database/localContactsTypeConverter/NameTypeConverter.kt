package de.benkralex.socius.data.contacts.local.database.localContactsTypeConverter

import androidx.room.TypeConverter
import de.benkralex.socius.data.model.Name
import kotlinx.serialization.json.Json

class NameTypeConverter {
    private val json = Json { prettyPrint = true }

    @TypeConverter
    fun fromName(value: Name): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toName(string: String): Name? {
        return try {
            json.decodeFromString<Name>(string)
        } catch (e: Exception) {
            null
        }
    }
}