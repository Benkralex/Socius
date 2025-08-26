package de.benkralex.socius.data.contacts.system.load

import android.content.ContentResolver
import android.provider.ContactsContract
import de.benkralex.socius.data.PhoneNumber

fun loadPhoneNumbersBatch(contentResolver: ContentResolver, contactIds: List<String>): Map<String, List<PhoneNumber>> {
    val result = mutableMapOf<String, MutableList<PhoneNumber>>()

    if (contactIds.isEmpty()) return result

    val selection = "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} IN (${contactIds.joinToString(",")})"

    val cursor = contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null,
        selection,
        null,
        null
    )

    cursor?.use { item ->
        val contactIdIndex = item.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
        val numberIndex = item.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
        val typeIndex = item.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)
        val labelIndex = item.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL)

        while (item.moveToNext()) {
            val contactId = item.getString(contactIdIndex)
            val number = if (numberIndex != -1) item.getString(numberIndex) else null
            val type = if (typeIndex != -1) item.getInt(typeIndex) else 0
            val label = if (labelIndex != -1) item.getString(labelIndex) else null

            val typeStr = when (type) {
                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> "mobile"
                ContactsContract.CommonDataKinds.Phone.TYPE_HOME -> "home"
                ContactsContract.CommonDataKinds.Phone.TYPE_WORK -> "work"
                ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME -> "fax_home"
                ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK -> "fax_work"
                ContactsContract.CommonDataKinds.Phone.TYPE_PAGER -> "pager"
                ContactsContract.CommonDataKinds.Phone.TYPE_OTHER -> "other"
                ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM -> "custom"
                ContactsContract.CommonDataKinds.Phone.TYPE_ASSISTANT -> "assistant"
                ContactsContract.CommonDataKinds.Phone.TYPE_CALLBACK -> "callback"
                ContactsContract.CommonDataKinds.Phone.TYPE_CAR -> "car"
                ContactsContract.CommonDataKinds.Phone.TYPE_COMPANY_MAIN -> "company_main"
                ContactsContract.CommonDataKinds.Phone.TYPE_ISDN -> "isdn"
                ContactsContract.CommonDataKinds.Phone.TYPE_MAIN -> "main"
                ContactsContract.CommonDataKinds.Phone.TYPE_MMS -> "mms"
                ContactsContract.CommonDataKinds.Phone.TYPE_OTHER_FAX -> "fax_other"
                ContactsContract.CommonDataKinds.Phone.TYPE_RADIO -> "radio"
                ContactsContract.CommonDataKinds.Phone.TYPE_TELEX -> "telex"
                ContactsContract.CommonDataKinds.Phone.TYPE_TTY_TDD -> "tty_tdd"
                ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE -> "work_mobile"
                ContactsContract.CommonDataKinds.Phone.TYPE_WORK_PAGER -> "work_pager"
                else -> "unknown"
            }

            val phoneNumbersForContact = result.getOrPut(contactId) { mutableListOf() }
            val isDuplicate = phoneNumbersForContact.any { it.number.replace(" ", "") == number?.replace(" ", "") }

            if (!isDuplicate && number != null) {
                phoneNumbersForContact.add(PhoneNumber(number, typeStr, label))
            }
        }
    }

    return result
}