package de.benkralex.socius.pages.newContact

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

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
}