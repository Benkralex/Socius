package de.benkralex.socius.ui.components.editContact

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import de.benkralex.socius.data.model.Contact
import de.benkralex.socius.data.model.Email
import de.benkralex.socius.data.model.Type

class EditEmailsState {
    val showFields by derivedStateOf { count > 0 }
    var count: Int by mutableIntStateOf(0)
    var values: MutableList<MutableState<String>> by mutableStateOf(mutableListOf())
    var types: MutableList<MutableState<Type.Email>> by mutableStateOf(mutableListOf())

    fun hasRelevantData(): Boolean {
        return values.any { it.value.isNotBlank() }
    }

    fun isRelevant(i: Int): Boolean {
        return values[i].value.isNotBlank()
    }
    
    fun getRelevantData(): List<Email> {
        val phoneNumbers: MutableList<Email> = mutableListOf()
        for (i in 0..< count) {
            if (isRelevant(i)) {
                phoneNumbers.add(
                    Email(
                        value = values[i].value.trim(),
                        type = types[i].value,
                    )
                )
            }
        }
        return phoneNumbers
    }

    fun addNew() {
        values.add(mutableStateOf(""))
        types.add(mutableStateOf(Type.Email.HOME))
        count++
    }

    fun loadFromContact(contact: Contact) {
        for (email: Email in contact.emails) {
            count++
            values.add(mutableStateOf(email.value))
            types.add(mutableStateOf(email.type))
        }
    }

    fun reset() {
        count = 0
        values.clear()
        types.clear()
    }
}