package de.benkralex.socius.data.contacts

import de.benkralex.socius.MainActivity
import de.benkralex.socius.data.contacts.local.database.LocalContactsEntity
import de.benkralex.socius.data.model.Contact
import de.benkralex.socius.data.model.ContactOrigin
import de.benkralex.socius.data.syncadapter.SyncManager

suspend fun editStarredStatus(contact: Contact, isStarred: Boolean): Boolean {
    val mainActivity = MainActivity
    val id = contact.id ?: return false
    when (contact.origin) {
        ContactOrigin.LOCAL -> {
            mainActivity.localContactsDao.updateStarredStatus(
                id = id,
                isStarred = isStarred
            )
        }
        ContactOrigin.TEMPORARY -> return false
        ContactOrigin.NEW -> return false
    }
    loadAllContacts()
    SyncManager.requestSync(MainActivity.instance)
    return true
}

suspend fun deleteContact(contact: Contact): Boolean {
    val mainActivity = MainActivity
    val id = contact.id ?: return false
    when (contact.origin) {
        ContactOrigin.LOCAL -> {
            mainActivity.localContactsDao.deleteById(id)
        }
        ContactOrigin.TEMPORARY -> return false
        ContactOrigin.NEW -> return false
    }
    loadAllContacts()
    SyncManager.requestSync(MainActivity.instance)
    return true
}

suspend fun deleteContacts(contacts: List<Contact>): Map<Contact, Boolean> {
    val returnMap: MutableMap<Contact, Boolean> = mutableMapOf()
    val mainActivity = MainActivity
    for (contact in contacts) {
        val id = contact.id
        if (id == null) {
            returnMap[contact] = false
            continue
        }
        when (contact.origin) {
            ContactOrigin.LOCAL -> {
                mainActivity.localContactsDao.deleteById(id)
                returnMap[contact] = true
            }
            ContactOrigin.TEMPORARY -> returnMap[contact] = false
            ContactOrigin.NEW -> returnMap[contact] = false
        }
    }
    loadAllContacts()
    SyncManager.requestSync(MainActivity.instance)
    return returnMap
}

suspend fun editContact(contact: Contact): Boolean {
    val mainActivity = MainActivity
    val id = contact.id ?: return false
    when (contact.origin) {
        ContactOrigin.LOCAL -> {
            val entity = LocalContactsEntity.fromContact(contact)
            mainActivity.localContactsDao.update(entity)
        }
        ContactOrigin.TEMPORARY -> return false
        ContactOrigin.NEW -> return false
    }
    loadAllContacts()
    SyncManager.requestSync(MainActivity.instance)
    return true
}