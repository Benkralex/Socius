package de.benkralex.socius.data.contacts.local.database.localContactsTypeConverter

import androidx.room.TypeConverter
import de.benkralex.socius.data.model.Address
import kotlinx.serialization.json.Json

class AddressTypeConverter {
    private val json = Json { prettyPrint = true }

    @TypeConverter
    fun fromAddress(value: List<Address>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toAddress(string: String): List<Address>? {
        return try {
            json.decodeFromString<List<Address>>(string)
        } catch (_: Exception) {
            null
        }
    }
}