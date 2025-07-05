package de.benkralex.socius.backend.settings

import de.benkralex.socius.R
import de.benkralex.socius.backend.Contact

val nameFormats = listOf(
    Format(format = "%givenName% %familyName%", displayNameResource = R.string.settings_name_format_first_last, id = 0),
    Format(format = "%familyName%, %givenName%", displayNameResource = R.string.settings_name_format_last_first, id = 1),
    Format(format = "%givenName% %middleName% %familyName%", displayNameResource = R.string.settings_name_format_first_middle_last, id = 2),
    Format(format = "%familyName%, %givenName% %middleName%", displayNameResource = R.string.settings_name_format_last_first_middle, id = 3),
    Format(format = "%prefix% %givenName% %middleName% %familyName% %suffix%", displayNameResource = R.string.settings_name_format_full_first_last, id = 4),
    Format(format = "%prefix% %familyName%, %givenName% %middleName% %suffix%", displayNameResource = R.string.settings_name_format_full_last_first, id = 5),
)

fun getFormattedName(contact: Contact): String {
    /*
    contact.displayName: "%displayName%"
    contact.familyName: "%familyName%"
    contact.middleName: "%middleName%"
    contact.givenName: "%givenName%"
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
    formattedName = formattedName.replace("  ", " ") // Replace double spaces with single space
    return formattedName
}