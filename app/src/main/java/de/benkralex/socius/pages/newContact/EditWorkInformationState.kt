package de.benkralex.socius.pages.newContact

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class EditWorkInformationState {
    val showFields by derivedStateOf {
        showJobTitleTextField ||
                showDepartmentTextField ||
                showOrganizationTextField
    }
    var jobTitle by mutableStateOf("")
    var showJobTitleTextField by mutableStateOf(false)

    var department by mutableStateOf("")
    var showDepartmentTextField by mutableStateOf(false)

    var organization by mutableStateOf("")
    var showOrganizationTextField by mutableStateOf(false)

    fun hasRelevantData(): Boolean {
        return jobTitle.isNotBlank() || department.isNotBlank() || organization.isNotBlank()
    }
}