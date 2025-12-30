package de.benkralex.socius.pages.newContact

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import de.benkralex.socius.MainActivity
import de.benkralex.socius.data.contacts.loadAllContacts
import de.benkralex.socius.data.contacts.local.database.LocalContactsEntity
import de.benkralex.socius.sync.SyncManager
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class NewContactViewModel : ViewModel() {
    var showAddFieldBottomModal by mutableStateOf(false)

    val structuredNameState: EditStructuredNameState = EditStructuredNameState()
    val workInformationState: EditWorkInformationState = EditWorkInformationState()
    val emailsState: EditEmailsState = EditEmailsState()
    val phoneNumbersState: EditPhoneNumbersState = EditPhoneNumbersState()
    val eventsState: EditEventsState = EditEventsState()
    val postalAddressesState: EditPostalAddressesState = EditPostalAddressesState()
    var isStarred by mutableStateOf(false)

    var isSaving by mutableStateOf(false)
    var error by mutableStateOf(false)

    fun checkEmpty(): Boolean {
        return !structuredNameState.hasRelevantData()
                && !workInformationState.hasRelevantData()
                && !emailsState.hasRelevantData()
                && !phoneNumbersState.hasRelevantData()
                && !eventsState.hasRelevantData()
                && !postalAddressesState.hasRelevantData()
    }

    fun saveContact() {
        if (checkEmpty()) {
            error = true
            return
        }
        Thread {
            runBlocking {
                launch {
                    isSaving = true
                    error = false
                    MainActivity.localContactsDao.insert(
                        LocalContactsEntity(
                            prefix = structuredNameState.prefix.trim().ifBlank { null },
                            givenName = structuredNameState.givenName.trim().ifBlank { null },
                            middleName = structuredNameState.middleName.trim().ifBlank { null },
                            familyName = structuredNameState.familyName.trim().ifBlank { null },
                            suffix = structuredNameState.suffix.trim().ifBlank { null },
                            nickname = structuredNameState.nickname.trim().ifBlank { null },

                            jobTitle = workInformationState.jobTitle.trim().ifBlank { null },
                            department = workInformationState.department.trim().ifBlank { null },
                            organization = workInformationState.organization.trim().ifBlank { null },

                            emails = emailsState.getRelevantData(),
                            phoneNumbers = phoneNumbersState.getRelevantData(),
                            events = eventsState.getRelevantData(),
                            addresses = postalAddressesState.getRelevantData(),

                            isStarred = isStarred,
                        )
                    )
                    loadAllContacts()
                    // Sync local contacts to system after saving
                    SyncManager.requestSync(MainActivity.instance)
                    isSaving = false
                }
            }
        }.start()
    }
}