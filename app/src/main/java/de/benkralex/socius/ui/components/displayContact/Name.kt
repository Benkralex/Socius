package de.benkralex.socius.ui.components.displayContact

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import de.benkralex.socius.data.model.Contact
import de.benkralex.socius.data.settings.getFormattedFullName
import de.benkralex.socius.data.settings.getFormattedName
import de.benkralex.socius.data.settings.preferNickname

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
fun NameWidget(contact: Contact) {
    var formattedName = getFormattedName(contact)
    //Add linebreaks
    formattedName = buildString {
        val words = formattedName.split("\\s+".toRegex()).filter { it.isNotBlank() }
        words.forEachIndexed { index, word ->
            append(word)
            if (word.length > 3) {
                append("\n")
            } else if (index != words.lastIndex) {
                append(" ")
            }
        }
    }.trimEnd()
    Text(
        text = formattedName,
        style = MaterialTheme.typography.displayLarge,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        textAlign = TextAlign.Center,
    )

    val phoneticName: String = listOfNotNull(
        contact.name.phoneticFirstname?.trim(),
        contact.name.phoneticSecondName?.trim(),
        contact.name.phoneticLastname?.trim(),
    ).joinToString(" ").trim()
    val nameOrNickname: String =
        if (getFormattedFullName(contact).trim() != getFormattedName(contact).trim())
            getFormattedFullName(contact).trim()
        else if (!preferNickname)
            contact.name.nickname?.trim() ?: ""
        else
            ""
    if (phoneticName.isNotBlank() || nameOrNickname.isNotBlank()) {
        Text(
            text = if (phoneticName.isNotBlank() && nameOrNickname.isNotBlank()) "$nameOrNickname • $phoneticName" else "$nameOrNickname$phoneticName",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
    }
}