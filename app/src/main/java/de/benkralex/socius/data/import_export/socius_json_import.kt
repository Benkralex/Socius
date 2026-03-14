package de.benkralex.socius.data.import_export

import de.benkralex.socius.data.Contact
import kotlinx.serialization.json.Json

fun sociusJsonToContacts(file: List<String>): List<Contact> {
    if (file.isEmpty()) return emptyList()

    val rawJson = file.joinToString("\n").trim()
    if (rawJson.isEmpty()) return emptyList()

    val json = Json { ignoreUnknownKeys = true }

    return try {
        json.decodeFromString<List<Contact>>(rawJson)
    } catch (_: Exception) {
        emptyList()
    }
}