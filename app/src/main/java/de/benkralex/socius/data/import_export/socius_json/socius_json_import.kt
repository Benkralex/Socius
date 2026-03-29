package de.benkralex.socius.data.import_export.socius_json

import android.util.Log
import de.benkralex.socius.data.model.Contact
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

fun sociusJsonToContacts(file: List<String>): List<Contact> {
    if (file.isEmpty()) return emptyList()

    val rawJson = file.joinToString("\n").trim()
    if (rawJson.isEmpty()) return emptyList()

    val json = Json { ignoreUnknownKeys = true }

    return try {
        val versionString = json.decodeFromString<JsonObject>(rawJson)["first"]?.jsonPrimitive?.content
        when (versionString) {
            "Version1" -> json.decodeFromString<Pair<String, List<de.benkralex.socius.data.model.old.Contact>>>(rawJson).second.map { it.toNewContact() }
            "Version2" -> json.decodeFromString<Pair<String, List<Contact>>>(rawJson).second
            else -> json.decodeFromString<Pair<String, List<de.benkralex.socius.data.model.old.Contact>>>(rawJson).second.map { it.toNewContact() }
        }
    } catch (_: Exception) {
        try {
            val contacts = json.decodeFromString<List<de.benkralex.socius.data.model.old.Contact>>(rawJson)
            contacts.map { it.toNewContact() }
        } catch (e: Exception) {
            Log.e("SociusImport", "Error parsing Version 1 fallback", e)
            emptyList()
        }
    }
}