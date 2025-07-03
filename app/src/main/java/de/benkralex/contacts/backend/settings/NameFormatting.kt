package de.benkralex.contacts.backend.settings

import de.benkralex.contacts.R
import de.benkralex.contacts.backend.Contact

val nameFormats = listOf(
    Format(format = "%givenName% %familyName%", displayNameResource = R.string.settings_name_format_first_last, id = 0),
    Format(format = "%familyName%, %givenName%", displayNameResource = R.string.settings_name_format_last_first, id = 1),
)

fun getFormattedName(contact: Contact): String {
    /*
    contact.displayName: "%displayName%"
    contact.familyName: "%familyName%"
    contact.middleName: "%middleName%"
    contact.givenName: "%givenName%"
    contact.displayName: "%displayName%"
    contact.nickname: "%nickname%"
    contact.phoneticFamilyName: "%phoneticFamilyName%"
    contact.phoneticMiddleName: "%phoneticMiddleName%"
    contact.phoneticGivenName: "%phoneticGivenName%"
    contact.prefix: "%prefix%"
    contact.suffix: "%suffix%"
    */
    var formattedName = nameFormats.first { it.id == nameFormat }.format
    formattedName = formattedName.replace("%displayName%", contact.displayName ?: "")
    formattedName = formattedName.replace("%familyName%", contact.familyName ?: "")
    formattedName = formattedName.replace("%middleName%", contact.middleName ?: "")
    formattedName = formattedName.replace("%givenName%", contact.givenName ?: "")
    formattedName = formattedName.replace("%nickname%", contact.nickname ?: "")
    formattedName = formattedName.replace("%phoneticFamilyName%", contact.phoneticFamilyName ?: "")
    formattedName = formattedName.replace("%phoneticMiddleName%", contact.phoneticMiddleName ?: "")
    formattedName = formattedName.replace("%phoneticGivenName%", contact.phoneticGivenName ?: "")
    formattedName = formattedName.replace("%prefix%", contact.prefix ?: "")
    formattedName = formattedName.replace("%suffix%", contact.suffix ?: "")
    formattedName = formattedName.trim(*charsToTrim)
    return formattedName
}