package de.benkralex.socius.data.contacts.local.load

import de.benkralex.socius.MainActivity
import de.benkralex.socius.data.Contact

suspend fun getLocalContacts(mainActivity: MainActivity.Companion): List<Contact> {
    return mainActivity.localContactsDao.getAll().map { it.toContact(mainActivity.instance) }
}