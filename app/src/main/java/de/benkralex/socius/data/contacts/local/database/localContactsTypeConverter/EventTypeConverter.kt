package de.benkralex.socius.data.contacts.local.database.localContactsTypeConverter

import androidx.room.TypeConverter
import de.benkralex.socius.data.model.Event
import kotlinx.serialization.json.Json

class EventTypeConverter {
    private val json = Json { prettyPrint = true }

    @TypeConverter
    fun fromEvent(value: List<Event>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toEvent(string: String): List<Event>? {
        return try {
            json.decodeFromString<List<Event>>(string)
        } catch (e: Exception) {
            null
        }
    }
}