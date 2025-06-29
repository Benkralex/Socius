package de.benkralex.contacts.backend

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import de.benkralex.contacts.R

var nameFormat: String = "%familyName%, %givenName%"
var nameFormatWithoutFamilyName: String = "%givenName%"
var dateFormat: String = "%dayLeadingZero%. %monthName% %yearLong%"


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
    var formattedName = nameFormat
    if (contact.familyName == null) {
        formattedName = nameFormatWithoutFamilyName
    }
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
    formattedName = formattedName.trim()
    return formattedName
}

@Composable
fun getFormattedDate(day: Int, month: Int, year: Int): String {
    /*
    day: "%dayLeadingZero%"
    day: "%dayNoLeadingZero%"
    month: "%monthNoLeadingZero%"
    month: "%monthLeadingZero%"
    month: "%monthName%"
    year: "%yearLong%"
    year: "%yearShort%"
    */
    val months = listOf(stringResource(R.string.month_january),
                        stringResource(R.string.month_february),
                        stringResource(R.string.month_march),
                        stringResource(R.string.month_april),
                        stringResource(R.string.month_may),
                        stringResource(R.string.month_june),
                        stringResource(R.string.month_july),
                        stringResource(R.string.month_august),
                        stringResource(R.string.month_september),
                        stringResource(R.string.month_october),
                        stringResource(R.string.month_november),
                        stringResource(R.string.month_december),
        )
    var formattedDate = dateFormat
    formattedDate = formattedDate.replace("%dayLeadingZero%", day.toString().padStart(2, '0'))
    formattedDate = formattedDate.replace("%dayNoLeadingZero%", day.toString())
    formattedDate = formattedDate.replace("%monthLeadingZero%", month.toString().padStart(2, '0'))
    formattedDate = formattedDate.replace("%monthNoLeadingZero%", month.toString())
    formattedDate = formattedDate.replace("%monthName%", months[month])
    if (year != 0) {
        formattedDate = formattedDate.replace("%yearLong%", year.toString())
        formattedDate = formattedDate.replace("%yearShort%", (year % 100).toString().padStart(2, '0'))
    } else {
        formattedDate = formattedDate.replace("%yearLong%", "")
        formattedDate = formattedDate.replace("%yearShort%", "")
    }
    formattedDate = formattedDate.trim()
    return formattedDate
}