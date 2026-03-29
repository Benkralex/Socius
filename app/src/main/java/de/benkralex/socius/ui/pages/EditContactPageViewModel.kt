package de.benkralex.socius.ui.pages

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import de.benkralex.socius.MainActivity
import de.benkralex.socius.data.contacts.editContact
import de.benkralex.socius.data.contacts.loadAllContacts
import de.benkralex.socius.data.contacts.local.database.LocalContactsEntity
import de.benkralex.socius.data.model.Contact
import de.benkralex.socius.data.model.ContactOrigin
import de.benkralex.socius.data.model.Job
import de.benkralex.socius.data.model.Name
import de.benkralex.socius.data.model.ProfilePicture
import de.benkralex.socius.data.syncadapter.SyncManager
import de.benkralex.socius.ui.components.editContact.EditAddressesState
import de.benkralex.socius.ui.components.editContact.EditEmailsState
import de.benkralex.socius.ui.components.editContact.EditEventsState
import de.benkralex.socius.ui.components.editContact.EditPhonesState
import de.benkralex.socius.ui.components.editContact.EditProfilePictureState
import de.benkralex.socius.ui.components.editContact.EditStructuredNameState
import de.benkralex.socius.ui.components.editContact.EditWorkInformationState
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class EditContactPageViewModel : ViewModel() {
    var showAddFieldBottomModal by mutableStateOf(false)

    val profilePictureState: EditProfilePictureState = EditProfilePictureState()
    val structuredNameState: EditStructuredNameState = EditStructuredNameState()
    val workInformationState: EditWorkInformationState = EditWorkInformationState()
    val emailsState: EditEmailsState = EditEmailsState()
    val phonesState: EditPhonesState = EditPhonesState()
    val eventsState: EditEventsState = EditEventsState()
    val addressesState: EditAddressesState = EditAddressesState()
    var isStarred by mutableStateOf(false)

    var isSaving by mutableStateOf(false)
    var error by mutableStateOf(false)
    var isInitialized by mutableStateOf(false)

    var loadedContact: Contact? = null

    fun loadFromContact(contact: Contact) {
        isInitialized = true
        loadedContact = contact
        profilePictureState.loadFromContact(contact)
        if (contact.origin == ContactOrigin.NEW) return
        structuredNameState.loadFromContact(contact)
        workInformationState.loadFromContact(contact)
        emailsState.loadFromContact(contact)
        phonesState.loadFromContact(contact)
        eventsState.loadFromContact(contact)
        addressesState.loadFromContact(contact)
        isStarred = contact.isStarred
    }

    fun hasNoChanges(): Boolean {
        return if (loadedContact?.origin == ContactOrigin.NEW)
            !profilePictureState.hasRelevantData()
                    && !structuredNameState.hasRelevantData()
                    && !workInformationState.hasRelevantData()
                    && !emailsState.hasRelevantData()
                    && !phonesState.hasRelevantData()
                    && !eventsState.hasRelevantData()
                    && !addressesState.hasRelevantData()
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
                    if (loadedContact!!.origin == ContactOrigin.LOCAL && loadedContact!!.origin == ContactOrigin.NEW) {
                        MainActivity.localContactsDao.insert(
                            LocalContactsEntity(
                                name = Name(
                                    prefix = structuredNameState.prefix.trim().ifBlank { null },
                                    firstname = structuredNameState.firstname.trim().ifBlank { null },
                                    secondName = structuredNameState.secondName.trim().ifBlank { null },
                                    lastname = structuredNameState.lastname.trim().ifBlank { null },
                                    suffix = structuredNameState.suffix.trim().ifBlank { null },
                                    nickname = structuredNameState.nickname.trim().ifBlank { null },
                                ),
                                job = Job(
                                    jobTitle = workInformationState.jobTitle.trim()
                                        .ifBlank { null },
                                    department = workInformationState.department.trim()
                                        .ifBlank { null },
                                    organization = workInformationState.organization.trim()
                                        .ifBlank { null },
                                ),
                                emails = emailsState.getRelevantData(),
                                phoneNumbers = phonesState.getRelevantData(),
                                events = eventsState.getRelevantData(),
                                addresses = addressesState.getRelevantData(),
                                profilePicture = ProfilePicture(profilePictureState.picture),

                                isStarred = isStarred,
                            )
                        )
                        loadAllContacts()
                        SyncManager.requestSync(MainActivity.instance)
                        isSaving = false
                        return@launch
                    }
                    if (loadedContact!!.origin == ContactOrigin.LOCAL && loadedContact!!.origin != ContactOrigin.NEW) {
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
            name = Name(
                prefix = structuredNameState.prefix.trim().ifBlank { null },
                firstname = structuredNameState.firstname.trim().ifBlank { null },
                secondName = structuredNameState.secondName.trim().ifBlank { null },
                lastname = structuredNameState.lastname.trim().ifBlank { null },
                suffix = structuredNameState.suffix.trim().ifBlank { null },
                nickname = structuredNameState.nickname.trim().ifBlank { null },
            ),
            job = Job(
                jobTitle = workInformationState.jobTitle.trim().ifBlank { null },
                department = workInformationState.department.trim().ifBlank { null },
                organization = workInformationState.organization.trim().ifBlank { null },
            ),

            emails = emailsState.getRelevantData(),
            phoneNumbers = phonesState.getRelevantData(),
            events = eventsState.getRelevantData(),
            addresses = addressesState.getRelevantData(),

            profilePicture = ProfilePicture(profilePictureState.picture),

            isStarred = isStarred,
        )
    }

    fun reset() {
        showAddFieldBottomModal = false

        profilePictureState.reset()
        structuredNameState.reset()
        workInformationState.reset()
        emailsState.reset()
        phonesState.reset()
        eventsState.reset()
        addressesState.reset()
        isStarred = false

        isSaving = false
        error = false
        isInitialized = false

        loadedContact = null
    }
}