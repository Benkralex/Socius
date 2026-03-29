package de.benkralex.socius.data.settings

import de.benkralex.socius.R
import de.benkralex.socius.data.model.Contact
import de.benkralex.socius.data.model.Type

val nameFormats = listOf(
    Format(format = "%firstname% %lastname%", displayNameResource = R.string.settings_name_format_first_last, id = 0),
    Format(format = "%lastname%, %firstname%", displayNameResource = R.string.settings_name_format_last_first, id = 1),
    Format(format = "%firstname% %secondName% %lastname%", displayNameResource = R.string.settings_name_format_first_middle_last, id = 2),
    Format(format = "%lastname%, %firstname% %secondName%", displayNameResource = R.string.settings_name_format_last_first_middle, id = 3),
    Format(format = "%prefix% %firstname% %secondName% %lastname% %suffix%", displayNameResource = R.string.settings_name_format_full_first_last, id = 4),
    Format(format = "%prefix% %lastname%, %firstname% %secondName% %suffix%", displayNameResource = R.string.settings_name_format_full_last_first, id = 5),
)
val fullNameFormats = listOf(
    Format(format = "%prefix% %firstname% %secondName% %lastname% %suffix%", displayNameResource = R.string.settings_name_format_full_first_last, id = 0),
    Format(format = "%prefix% %lastname%, %firstname% %secondName% %suffix%", displayNameResource = R.string.settings_name_format_full_last_first, id = 1),
)
var noName: String? = null

fun getFormattedName(contact: Contact): String {
    if (!contact.name.nickname.isNullOrBlank() && preferNickname) return contact.name.nickname!!
    val formattedName = format(contact, nameFormats.first { it.id == nameFormat })
    if (formattedName.isBlank()) {
        //Fallback to phone or email
        if (contact.phoneNumbers.isNotEmpty()) {
            return (contact.phoneNumbers.firstOrNull { it.type == Type.Phone.HOME } ?: contact.phoneNumbers.first()).value
        }
        if (contact.emails.isNotEmpty()) {
            return (contact.emails.firstOrNull { it.type == Type.Email.HOME } ?: contact.emails.first()).value
        }
        return noName ?: ""
    }
    return formattedName
}

fun getFormattedFullName(contact: Contact): String {
    return format(contact, fullNameFormats.first { it.id == fullNameFormat })
}

fun format(contact: Contact, format: Format): String {
    /*
    contact.displayName: "%displayName%"
    contact.lastname: "%lastname%"
    contact.secondName: "%secondName%"
    contact.firstname: "%firstname%"
    contact.nickname: "%nickname%"
    contact.phoneticLastname: "%phoneticLastname%"
    contact.phoneticSecondName: "%phoneticSecondName%"
    contact.phoneticFirstname: "%phoneticFirstname%"
    contact.prefix: "%prefix%"
    contact.suffix: "%suffix%"
    */
    var formattedName = format.format
    formattedName = formattedName.replace("%displayName%", contact.name.displayName ?: "")
    formattedName = formattedName.replace("%lastname%", contact.name.lastname ?: "")
    formattedName = formattedName.replace("%secondName%", contact.name.secondName ?: "")
    formattedName = formattedName.replace("%firstname%", contact.name.firstname ?: "")
    formattedName = formattedName.replace("%nickname%", contact.name.nickname ?: "")
    formattedName = formattedName.replace("%phoneticLastname%", contact.name.phoneticLastname ?: "")
    formattedName = formattedName.replace("%phoneticSecondName%", contact.name.phoneticSecondName ?: "")
    formattedName = formattedName.replace("%phoneticFirstname%", contact.name.phoneticFirstname ?: "")
    formattedName = formattedName.replace("%prefix%", contact.name.prefix ?: "")
    formattedName = formattedName.replace("%suffix%", contact.name.suffix ?: "")
    formattedName = formattedName.trim(*charsToTrim)
    formattedName = formattedName.replace(" ,", ",")
    while (formattedName.contains("  ")) {
        formattedName = formattedName.replace("  ", " ")
    }
    return formattedName
}