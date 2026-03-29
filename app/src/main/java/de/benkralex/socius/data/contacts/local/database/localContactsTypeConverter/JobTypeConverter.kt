package de.benkralex.socius.data.contacts.local.database.localContactsTypeConverter

import androidx.room.TypeConverter
import de.benkralex.socius.data.model.Job
import kotlinx.serialization.json.Json

class JobTypeConverter {
    private val json = Json { prettyPrint = true }

    @TypeConverter
    fun fromJob(value: Job): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toJob(string: String): Job? {
        return try {
            json.decodeFromString<Job>(string)
        } catch (e: Exception) {
            null
        }
    }
}