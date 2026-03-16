package de.benkralex.socius.data.import_export.socius_json

import de.benkralex.socius.data.model.Contact
import kotlinx.serialization.json.Json

fun contactsToSociusJson(contacts: List<Contact>): List<String> {
    return listOf(Json { prettyPrint = true }.encodeToString(contacts))
}