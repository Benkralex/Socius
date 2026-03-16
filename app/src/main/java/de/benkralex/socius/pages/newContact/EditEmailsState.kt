package de.benkralex.socius.pages.newContact

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import de.benkralex.socius.data.model.Contact
import de.benkralex.socius.data.model.Email

class EditEmailsState {
    val showFields by derivedStateOf { count > 0 }
    var count: Int by mutableIntStateOf(0)
    var addresses: MutableList<MutableState<String>> by mutableStateOf(mutableListOf())
    var types: MutableList<MutableState<String>> by mutableStateOf(mutableListOf())
    var labels: MutableList<MutableState<String>> by mutableStateOf(mutableListOf())

    fun hasRelevantData(): Boolean {
        return addresses.any { it.value.isNotBlank() }
    }

    fun isRelevant(i: Int): Boolean {
        return addresses[i].value.isNotBlank()
    }
    
    fun getRelevantData(): List<Email> {
        val phoneNumbers: MutableList<Email> = mutableListOf()
        for (i in 0..<count) {
            if (isRelevant(i)) {
                phoneNumbers.add(
                    Email(
                        address = addresses[i].value.trim(),
                        type = types[i].value.trim().ifBlank { "home" },
                        label = labels[i].value.trim().ifBlank { null },
                    )
                )
            }
        }
        return phoneNumbers
    }

    fun addNew() {
        addresses.add(mutableStateOf(""))
        types.add(mutableStateOf("home"))
        labels.add(mutableStateOf(""))
        count++
    }

    fun loadFromContact(contact: Contact) {
        for (email: Email in contact.emails) {
            count++
            addresses.add(mutableStateOf(email.address))
            types.add(mutableStateOf(email.type))
            labels.add(mutableStateOf(email.label ?: ""))
        }
    }

    fun reset() {
        count = 0
        addresses.clear()
        types.clear()
        labels.clear()
    }
}