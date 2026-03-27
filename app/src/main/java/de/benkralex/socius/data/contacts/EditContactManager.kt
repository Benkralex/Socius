package de.benkralex.socius.data.contacts

import de.benkralex.socius.MainActivity
import de.benkralex.socius.data.model.Contact
import de.benkralex.socius.data.model.ContactOrigin
import de.benkralex.socius.data.contacts.local.database.LocalContactsEntity
import de.benkralex.socius.data.syncadapter.SyncManager

suspend fun editStarredStatus(contact: Contact, isStarred: Boolean): Boolean {
    val mainActivity = MainActivity
    val id = contact.id.toIntOrNull() ?: return false
    when (contact.origin) {
        ContactOrigin.LOCAL -> {
            mainActivity.localContactsDao.updateStarredStatus(
                id = id,
                isStarred = isStarred
            )
        }
        ContactOrigin.IMPORT -> return false
    }
    loadAllContacts()
    SyncManager.requestSync(MainActivity.instance)
    return true
}

suspend fun deleteContact(contact: Contact): Boolean {
    val mainActivity = MainActivity
    val id = contact.id.toIntOrNull() ?: return false
    when (contact.origin) {
        ContactOrigin.LOCAL -> {
            mainActivity.localContactsDao.deleteById(id)
        }
        ContactOrigin.IMPORT -> return false
    }
    loadAllContacts()
    SyncManager.requestSync(MainActivity.instance)
    return true
}

suspend fun deleteContacts(contacts: List<Contact>): Map<Contact, Boolean> {
    val returnMap: MutableMap<Contact, Boolean> = mutableMapOf()
    val mainActivity = MainActivity
    for (contact in contacts) {
        val id = contact.id.toIntOrNull()
        if (id == null) {
            returnMap[contact] = false
            continue
        }
        when (contact.origin) {
            ContactOrigin.LOCAL -> {
                mainActivity.localContactsDao.deleteById(id)
                returnMap[contact] = true
            }
            ContactOrigin.IMPORT -> returnMap[contact] = false
        }
    }
    loadAllContacts()
    SyncManager.requestSync(MainActivity.instance)
    return returnMap
}

suspend fun editContact(contact: Contact): Boolean {
    val mainActivity = MainActivity
    val id = contact.id.toIntOrNull() ?: return false
    when (contact.origin) {
        ContactOrigin.LOCAL -> {
            val entity = LocalContactsEntity(
                id = id,
                displayName = contact.displayName,
                prefix = contact.prefix,
                givenName = contact.givenName,
                middleName = contact.middleName,
                familyName = contact.familyName,
                suffix = contact.suffix,
                nickname = contact.nickname,
                phoneticGivenName = contact.phoneticGivenName,
                phoneticMiddleName = contact.phoneticMiddleName,
                phoneticFamilyName = contact.phoneticFamilyName,
                organization = contact.organization,
                department = contact.department,
                jobTitle = contact.jobTitle,
                note = contact.note,
                isStarred = contact.isStarred,
                photoUri = contact.photoUri,
                thumbnailUri = contact.thumbnailUri,
                phoneNumbers = contact.phoneNumbers,
                emails = contact.emails,
                addresses = contact.addresses,
                websites = contact.websites,
                relations = contact.relations,
                events = contact.events,
                groups = contact.groups,
                customFields = contact.customFields
            )
            mainActivity.localContactsDao.update(entity)
        }
        ContactOrigin.IMPORT -> return false
    }
    loadAllContacts()
    SyncManager.requestSync(MainActivity.instance)
    return true
}