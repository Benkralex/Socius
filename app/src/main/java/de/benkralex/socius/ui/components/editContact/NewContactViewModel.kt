package de.benkralex.socius.ui.components.editContact

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import de.benkralex.socius.MainActivity
import de.benkralex.socius.data.model.Contact
import de.benkralex.socius.data.model.ContactOrigin
import de.benkralex.socius.data.contacts.editContact
import de.benkralex.socius.data.contacts.loadAllContacts
import de.benkralex.socius.data.contacts.local.database.LocalContactsEntity
import de.benkralex.socius.data.settings.getFormattedName
import de.benkralex.socius.data.syncadapter.SyncManager
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
    var isInitialized by mutableStateOf(false)

    var loadedContact: Contact? = null

    fun loadFromContact(contact: Contact) {
        Log.d("DEBUG EDIT CONTACT", "Load from Contact aufgerufen: ${getFormattedName(contact)}")
        isInitialized = true
        loadedContact = contact
        if (contact.id == "new") return
        structuredNameState.loadFromContact(contact)
        workInformationState.loadFromContact(contact)
        emailsState.loadFromContact(contact)
        phoneNumbersState.loadFromContact(contact)
        eventsState.loadFromContact(contact)
        postalAddressesState.loadFromContact(contact)
        isStarred = contact.isStarred
    }

    fun hasNoChanges(): Boolean {
        return if (loadedContact?.id == "new")
            !structuredNameState.hasRelevantData()
                    && !workInformationState.hasRelevantData()
                    && !emailsState.hasRelevantData()
                    && !phoneNumbersState.hasRelevantData()
                    && !eventsState.hasRelevantData()
                    && !postalAddressesState.hasRelevantData()
        else
            loadedContact == getNewContact()
    }

    fun saveContact() {
        if (hasNoChanges()) {
            error = true
            return
        }
        Thread {
            runBlocking {
                launch {
                    isSaving = true
                    error = false
                    if (loadedContact == null || !isInitialized) {
                        error = true
                        isSaving = false
                        return@launch
                    }
                    if (loadedContact!!.origin == ContactOrigin.SYSTEM) {
                        error = true
                        isSaving = false
                        return@launch
                    }
                    if (loadedContact!!.origin == ContactOrigin.LOCAL && loadedContact!!.id == "new") {
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
                        SyncManager.requestSync(MainActivity.instance)
                        isSaving = false
                        return@launch
                    }
                    if (loadedContact!!.origin == ContactOrigin.LOCAL && loadedContact!!.id != "new") {
                        error = !editContact(getNewContact())
                        isSaving = false
                        return@launch
                    }
                    error = true
                    isSaving = false
                }
            }
        }.start()
    }

    private fun getNewContact(): Contact {
        return loadedContact!!.copy(
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
    }

    fun reset() {
        Log.d("DEBUG EDIT CONTACT", "Reset aufgerufen")
        showAddFieldBottomModal = false

        structuredNameState.reset()
        workInformationState.reset()
        emailsState.reset()
        phoneNumbersState.reset()
        eventsState.reset()
        postalAddressesState.reset()
        isStarred = false

        isSaving = false
        error = false
        isInitialized = false

        loadedContact = null
    }
}