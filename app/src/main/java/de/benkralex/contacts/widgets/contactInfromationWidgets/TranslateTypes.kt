package de.benkralex.contacts.widgets.contactInfromationWidgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import de.benkralex.contacts.R

@Composable
fun translateType(type: String, label: String?): String {
    return when (type) {
        "home" -> stringResource(R.string.type_home)
        "work" -> stringResource(R.string.type_work)
        "mobile" -> stringResource(R.string.type_mobile)
        "other" -> stringResource(R.string.type_other)
        "unknown" -> stringResource(R.string.type_unknown)
        "custom" -> label!!
        else -> type
    }
}