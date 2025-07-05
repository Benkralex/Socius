package de.benkralex.socius.backend.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import de.benkralex.socius.R

val dateFormats = listOf(
    Format(format = "%dayNoLeadingZero%.%monthNoLeadingZero%.%yearLong%", displayNameResource = R.string.settings_date_format_d_m_yyyy_dot, id = 0),
    Format(format = "%dayLeadingZero%.%monthLeadingZero%.%yearLong%", displayNameResource = R.string.settings_date_format_dd_mm_yyyy_dot, id = 1),
    Format(format = "%monthNoLeadingZero%/%dayNoLeadingZero%/%yearLong%", displayNameResource = R.string.settings_date_format_m_d_yyyy_slash, id = 2),
    Format(format = "%yearLong%-%monthLeadingZero%-%dayLeadingZero%", displayNameResource = R.string.settings_date_format_yyyy_mm_dd_hyphen, id = 3),
)

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
    var formattedDate = dateFormats.first { it.id == dateFormat }.format
    formattedDate = formattedDate.replace("%dayLeadingZero%", day.toString().padStart(2, '0'))
    formattedDate = formattedDate.replace("%dayNoLeadingZero%", day.toString())
    formattedDate = formattedDate.replace("%monthLeadingZero%", month.toString().padStart(2, '0'))
    formattedDate = formattedDate.replace("%monthNoLeadingZero%", month.toString())
    formattedDate = formattedDate.replace("%monthName%", months[month - 1])
    if (year != 0) {
        formattedDate = formattedDate.replace("%yearLong%", year.toString())
        formattedDate = formattedDate.replace("%yearShort%", (year % 100).toString().padStart(2, '0'))
    } else {
        formattedDate = formattedDate.replace("%yearLong%", "")
        formattedDate = formattedDate.replace("%yearShort%", "")
    }
    formattedDate = formattedDate.trim(*charsToTrim)
    return formattedDate
}