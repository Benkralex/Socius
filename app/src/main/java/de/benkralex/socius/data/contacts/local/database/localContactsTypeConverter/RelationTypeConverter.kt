package de.benkralex.socius.data.contacts.local.database.localContactsTypeConverter

import androidx.room.TypeConverter
import de.benkralex.socius.data.model.Relation
import kotlinx.serialization.json.Json

class RelationTypeConverter {
    private val json = Json { prettyPrint = true }

    @TypeConverter
    fun fromRelation(value: List<Relation>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toRelation(string: String): List<Relation>? {
        return try {
            json.decodeFromString<List<Relation>>(string)
        } catch (_: Exception) {
            null
        }
    }
}