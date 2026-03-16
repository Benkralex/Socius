package de.benkralex.socius.ui.import_export

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import de.benkralex.socius.R
import de.benkralex.socius.data.import_export.common.exportContacts
import de.benkralex.socius.data.import_export.google_csv.contactsToGoogleCsv
import de.benkralex.socius.data.import_export.socius_json.contactsToSociusJson
import de.benkralex.socius.data.model.Contact

@Composable
fun ExportDialog (
    onDismiss: () -> Unit = {},
    context: Context = LocalContext.current,
    contacts: List<Contact>,
) {
    var selectedOption: ImportExportOption by remember { mutableStateOf(ImportExportOption.SociusJson) }

    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Card {
            Column (
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(16.dp),
            ) {
                Text(stringResource(R.string.export_dialog_title))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedOption = ImportExportOption.SociusJson
                        },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = selectedOption == ImportExportOption.SociusJson,
                        onClick = {
                            selectedOption = ImportExportOption.SociusJson
                        }
                    )
                    Text(stringResource(R.string.import_export_format_socius_json))
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedOption = ImportExportOption.GoogleCsv
                        },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = selectedOption == ImportExportOption.GoogleCsv,
                        onClick = {
                            selectedOption = ImportExportOption.GoogleCsv
                        }
                    )
                    Text(stringResource(R.string.import_export_format_google_csv))
                }
                ElevatedButton (
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = onDismiss,
                ) {
                    Text(stringResource(R.string.cancel))
                }
                if (selectedOption != ImportExportOption.None) {
                    Button (
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = {
                            when (selectedOption) {
                                ImportExportOption.None -> {
                                    onDismiss()
                                }
                                ImportExportOption.SociusJson -> {
                                    exportContacts(
                                        context = context,
                                        fileLines = contactsToSociusJson(contacts),
                                        fileType = "application/json",
                                        fileName = "socius-export.json",
                                    )
                                    onDismiss()
                                }
                                ImportExportOption.GoogleCsv -> {
                                    exportContacts(
                                        context = context,
                                        fileLines = contactsToGoogleCsv(contacts),
                                        fileType = "text/comma-separated-values",
                                        fileName = "socius-google-csv-export.csv",
                                    )
                                    onDismiss()
                                }
                            }
                        },
                    ) {
                        Text(stringResource(R.string.next))
                    }
                }
            }
        }
    }
}