package de.benkralex.socius.pages.newContact

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import de.benkralex.socius.MainActivity
import de.benkralex.socius.data.contacts.loadContacts
import de.benkralex.socius.data.contacts.local.database.LocalContactsEntity
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class NewContactViewModel : ViewModel() {
    var showAddFieldBottomModal by mutableStateOf(false)

    //Name fields
    var prefix by mutableStateOf("")
    var showPrefixTextField by mutableStateOf(false)
    var givenName by mutableStateOf("")
    var showGivenNameTextField by mutableStateOf(true)
    var middleName by mutableStateOf("")
    var showMiddleNameTextField by mutableStateOf(false)
    var familyName by mutableStateOf("")
    var showFamilyNameTextField by mutableStateOf(true)
    var suffix by mutableStateOf("")
    var showSuffixTextField by mutableStateOf(false)
    var nickname by mutableStateOf("")
    var showNicknameTextField by mutableStateOf(false)

    //Job information fields
    val showWorkInformation by derivedStateOf {
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

    var isSaving by mutableStateOf(false)

    fun saveContact() {
        Thread {
            runBlocking {
                launch {
                    isSaving = true
                    MainActivity.localContactsDao.insert(
                        LocalContactsEntity(
                            prefix = prefix.ifBlank { null },
                            givenName = givenName.ifBlank { null },
                            middleName = middleName.ifBlank { null },
                            familyName = familyName.ifBlank { null },
                            suffix = suffix.ifBlank { null },
                            nickname = nickname.ifBlank { null },

                            jobTitle = jobTitle.ifBlank { null },
                            department = department.ifBlank { null },
                            organization = organization.ifBlank { null },
                        )
                    )
                    loadContacts()
                    isSaving = false
                }
            }
        }.start()
    }
}