package de.benkralex.socius.ui.components.editContact

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import de.benkralex.socius.data.model.Contact
import de.benkralex.socius.data.model.Phone
import de.benkralex.socius.data.model.Type

class EditPhonesState {
    val showFields by derivedStateOf { count > 0 }
    var count: Int by mutableIntStateOf(0)
    var values: MutableList<MutableState<String>> by mutableStateOf(mutableListOf())
    var types: MutableList<MutableState<Type.Phone>> by mutableStateOf(mutableListOf())

    fun hasRelevantData(): Boolean {
        return values.any { it.value.isNotBlank() }
    }

    fun isRelevant(i: Int): Boolean {
        return values[i].value.isNotBlank()
    }
    
    fun getRelevantData(): List<Phone> {
        val phoneNumbers: MutableList<Phone> = mutableListOf()
        for (i in 0..<count) {
            if (isRelevant(i)) {
                phoneNumbers.add(
                    Phone(
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
        types.add(mutableStateOf(Type.Phone.HOME))
        count++
    }

    fun loadFromContact(contact: Contact) {
        for (num: Phone in contact.phoneNumbers) {
            count++
            values.add(mutableStateOf(num.value))
            types.add(mutableStateOf(num.type))
        }
    }

    fun reset() {
        count = 0
        values.clear()
        types.clear()
    }
}