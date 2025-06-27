package de.benkralex.contacts.backend

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.ContactsContract

fun getAndroidSystemContacts(context: Context): List<Pair<String, Bitmap?>> {
    val contacts = mutableListOf<Pair<String, Bitmap?>>()

    val contentResolver = context.contentResolver
    val uri = ContactsContract.Contacts.CONTENT_URI

    val cursor = contentResolver.query(
        uri,
        null,
        null,
        null,
        "${ContactsContract.Contacts.DISPLAY_NAME_PRIMARY} ASC"
    )

    cursor?.use {
        val nameIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
        val idIndex = it.getColumnIndex(ContactsContract.Contacts._ID)
        val hasPhotoIndex = it.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)

        while (it.moveToNext()) {
            val name = it.getString(nameIndex)
            val photoBitmap = if (hasPhotoIndex != -1) {
                it.getString(hasPhotoIndex)?.let { uriStr ->
                    try {
                        val photoUri = Uri.parse(uriStr)
                        contentResolver.openInputStream(photoUri)?.use { inputStream ->
                            BitmapFactory.decodeStream(inputStream)
                        }
                    } catch (e: Exception) {
                        null
                    }
                }
            } else null

            contacts.add(name to photoBitmap)
        }
    }

    return contacts
}