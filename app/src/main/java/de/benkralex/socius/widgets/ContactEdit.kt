package de.benkralex.socius.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.benkralex.socius.data.contacts.contacts
import de.benkralex.socius.data.settings.getFormattedName

@Composable
fun ContactEditWidget(
    modifier: Modifier = Modifier,
    contactId: Int? = null,
) {
    val contact = if (contactId == null) null else contacts[contactId]
    Column (
        modifier = modifier,
    ) {
        if (contact != null) {
            Text("Editing contact: ${getFormattedName(contact)}")
        } else {
            Text("Creating new contact")
        }
    }
}