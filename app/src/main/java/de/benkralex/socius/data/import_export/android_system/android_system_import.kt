package de.benkralex.socius.data.import_export.android_system

import android.content.Context
import de.benkralex.socius.data.import_export.android_system.load.getAndroidSystemContacts
import de.benkralex.socius.data.import_export.common.importContacts

suspend fun importAndroidSystemContacts(context: Context) {
    importContacts(getAndroidSystemContacts(context = context).map { it.toNewContact() })
}