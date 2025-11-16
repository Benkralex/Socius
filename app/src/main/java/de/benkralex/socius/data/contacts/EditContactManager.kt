package de.benkralex.socius.data.contacts

import de.benkralex.socius.MainActivity
import de.benkralex.socius.data.Contact
import de.benkralex.socius.data.ContactOrigin

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
    return true
}