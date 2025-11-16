package de.benkralex.socius.pages.newContact

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import de.benkralex.socius.MainActivity
import de.benkralex.socius.data.Email
import de.benkralex.socius.data.PhoneNumber
import de.benkralex.socius.data.contacts.loadAllContacts
import de.benkralex.socius.data.contacts.local.database.LocalContactsEntity
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class NewContactViewModel : ViewModel() {
    var showAddFieldBottomModal by mutableStateOf(false)

    //Name fields
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


    //Email fields
    val showEmailFields by derivedStateOf { emailCount > 0 }
    var emailCount: Int by mutableIntStateOf(0)
    var emailAddresses: MutableList<MutableState<String>> by mutableStateOf(mutableListOf())
    var emailTypes: MutableList<MutableState<String>> by mutableStateOf(mutableListOf())
    var emailLabels: MutableList<MutableState<String>> by mutableStateOf(mutableListOf())


    //Phone fields
    val showPhoneFields by derivedStateOf { phoneCount > 0 }
    var phoneCount: Int by mutableIntStateOf(0)
    var phoneNumbers: MutableList<MutableState<String>> by mutableStateOf(mutableListOf())
    var phoneTypes: MutableList<MutableState<String>> by mutableStateOf(mutableListOf())
    var phoneLabels: MutableList<MutableState<String>> by mutableStateOf(mutableListOf())


    var isSaving by mutableStateOf(false)

    fun saveContact() {
        Thread {
            runBlocking {
                launch {
                    isSaving = true
                    val emails: MutableList<Email> = mutableListOf()
                    for (i in 0 .. (emailCount-1)) {
                        if (emailAddresses[i].value.isNotBlank()) {
                            emails.add(
                                Email(
                                    address = emailAddresses[i].value,
                                    type = emailTypes[i].value.ifBlank { "home" },
                                    label = emailLabels[i].value.ifBlank { null },
                                )
                            )
                        }
                    }
                    val phones: MutableList<PhoneNumber> = mutableListOf()
                    for (i in 0 .. (phoneCount-1)) {
                        if (phoneNumbers[i].value.isNotBlank()) {
                            emails.add(
                                Email(
                                    address = phoneNumbers[i].value,
                                    type = phoneTypes[i].value.ifBlank { "home" },
                                    label = phoneLabels[i].value.ifBlank { null },
                                )
                            )
                        }
                    }
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

                            emails = emails,
                        )
                    )
                    loadAllContacts()
                    isSaving = false
                }
            }
        }.start()
    }
}