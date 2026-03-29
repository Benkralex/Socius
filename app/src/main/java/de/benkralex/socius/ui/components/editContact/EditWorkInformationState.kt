package de.benkralex.socius.ui.components.editContact

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import de.benkralex.socius.data.model.Contact

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

    fun loadFromContact(contact: Contact) {
        jobTitle = contact.job.jobTitle ?: ""
        showJobTitleTextField = !contact.job.jobTitle.isNullOrBlank()

        department = contact.job.department ?: ""
        showDepartmentTextField = !contact.job.department.isNullOrBlank()

        organization = contact.job.organization ?: ""
        showOrganizationTextField = !contact.job.organization.isNullOrBlank()
    }

    fun reset() {
        jobTitle = ""
        department = ""
        organization = ""
        showJobTitleTextField = false
        showDepartmentTextField = false
        showOrganizationTextField = false
    }
}