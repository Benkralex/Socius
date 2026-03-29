package de.benkralex.socius.data.import_export.socius_json

import de.benkralex.socius.data.model.Contact
import kotlinx.serialization.json.Json

private val json = Json { prettyPrint = true }

fun contactsToSociusJson(contacts: List<Contact>): List<String> {
    val export: Pair<String, List<Contact>> = "Version2" to contacts
    return listOf(json.encodeToString(export))
}