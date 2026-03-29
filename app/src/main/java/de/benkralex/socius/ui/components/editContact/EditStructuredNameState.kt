package de.benkralex.socius.ui.components.editContact

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import de.benkralex.socius.data.model.Contact

class EditStructuredNameState {
    var prefix by mutableStateOf("")
    var showPrefixTextField by mutableStateOf(false)

    var firstname by mutableStateOf("")

    var secondName by mutableStateOf("")
    var showSecondNameTextField by mutableStateOf(false)

    var lastname by mutableStateOf("")

    var suffix by mutableStateOf("")
    var showSuffixTextField by mutableStateOf(false)

    var nickname by mutableStateOf("")
    var showNicknameTextField by mutableStateOf(false)

    fun hasRelevantData(): Boolean {
        return prefix.isNotBlank() || firstname.isNotBlank() || secondName.isNotBlank() || lastname.isNotBlank() || suffix.isNotBlank() || nickname.isNotBlank()
    }

    fun loadFromContact(contact: Contact) {
        prefix = contact.name.prefix ?: ""
        showPrefixTextField = !contact.name.prefix.isNullOrBlank()

        firstname = contact.name.firstname ?: ""

        secondName = contact.name.secondName ?: ""
        showSecondNameTextField = !contact.name.secondName.isNullOrBlank()

        lastname = contact.name.lastname ?: ""

        suffix = contact.name.suffix ?: ""
        showSuffixTextField = !contact.name.suffix.isNullOrBlank()

        nickname = contact.name.nickname ?: ""
        showNicknameTextField = !contact.name.nickname.isNullOrBlank()
    }

    fun reset() {
        prefix = ""
        firstname = ""
        secondName = ""
        lastname = ""
        suffix = ""
        nickname = ""
        showPrefixTextField = false
        showSecondNameTextField = false
        showSuffixTextField = false
        showNicknameTextField = false
    }
}