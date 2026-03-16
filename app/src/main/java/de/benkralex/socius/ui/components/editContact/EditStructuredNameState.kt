package de.benkralex.socius.ui.components.editContact

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import de.benkralex.socius.data.model.Contact

class EditStructuredNameState {
    var prefix by mutableStateOf("")
    var showPrefixTextField by mutableStateOf(false)

    var givenName by mutableStateOf("")

    var middleName by mutableStateOf("")
    var showMiddleNameTextField by mutableStateOf(false)

    var familyName by mutableStateOf("")

    var suffix by mutableStateOf("")
    var showSuffixTextField by mutableStateOf(false)

    var nickname by mutableStateOf("")
    var showNicknameTextField by mutableStateOf(false)

    fun hasRelevantData(): Boolean {
        return prefix.isNotBlank() || givenName.isNotBlank() || middleName.isNotBlank() || familyName.isNotBlank() || suffix.isNotBlank() || nickname.isNotBlank()
    }

    fun loadFromContact(contact: Contact) {
        prefix = contact.prefix ?: ""
        showPrefixTextField = !contact.prefix.isNullOrBlank()

        givenName = contact.givenName ?: ""

        middleName = contact.middleName ?: ""
        showMiddleNameTextField = !contact.middleName.isNullOrBlank()

        familyName = contact.familyName ?: ""

        suffix = contact.suffix ?: ""
        showSuffixTextField = !contact.suffix.isNullOrBlank()

        nickname = contact.nickname ?: ""
        showNicknameTextField = !contact.nickname.isNullOrBlank()
    }

    fun reset() {
        prefix = ""
        givenName = ""
        middleName = ""
        familyName = ""
        suffix = ""
        nickname = ""
        showPrefixTextField = false
        showMiddleNameTextField = false
        showSuffixTextField = false
        showNicknameTextField = false
    }
}