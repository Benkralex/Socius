package de.benkralex.socius.data.contacts

import de.benkralex.socius.MainActivity
import de.benkralex.socius.data.Contact
import de.benkralex.socius.data.ContactOrigin
import de.benkralex.socius.data.contacts.local.database.LocalContactsEntity
import de.benkralex.socius.sync.SyncManager

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
        ContactOrigin.SYSTEM -> return false
        ContactOrigin.REMOTE -> return false
        ContactOrigin.URI -> return false
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
        ContactOrigin.SYSTEM -> return false
        ContactOrigin.REMOTE -> return false
        ContactOrigin.URI -> return false
    }
    loadAllContacts()
    SyncManager.requestSync(MainActivity.instance)
    return true
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
        ContactOrigin.SYSTEM -> return false
        ContactOrigin.REMOTE -> return false
        ContactOrigin.URI -> return false
    }
    loadAllContacts()
    SyncManager.requestSync(MainActivity.instance)
    return true
}