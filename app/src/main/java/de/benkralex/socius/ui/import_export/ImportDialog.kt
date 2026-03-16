package de.benkralex.socius.ui.import_export

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import de.benkralex.socius.R
import de.benkralex.socius.data.import_export.common.importContacts
import de.benkralex.socius.data.import_export.google_csv.googleCsvToContacts
import de.benkralex.socius.data.import_export.socius_json.sociusJsonToContacts
import de.benkralex.socius.ui.pages.getLinesOfFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

@Composable
fun ImportDialog (
    onDismiss: () -> Unit = {},
    context: Context = LocalContext.current,
) {
    var isImporting: Boolean by remember { mutableStateOf(false) }

    val activity = remember(context) { context as? ComponentActivity }
    val scope = rememberCoroutineScope()

    val googleCsvLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri == null || activity == null) return@rememberLauncherForActivityResult
        scope.launch(Dispatchers.IO) {
            try {
                val lines = getLinesOfFile(uri, activity)
                if (lines.isNotEmpty()) {
                    importContacts(googleCsvToContacts(lines))
                }
            } catch (th: IOException) {
                Log.e("ManagePage", "Unable to read import file", th)
            }
            isImporting = false
            onDismiss()
        }
    }

    val sociusJsonLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri == null || activity == null) return@rememberLauncherForActivityResult
        scope.launch(Dispatchers.IO) {
            try {
                val lines = getLinesOfFile(uri, activity)
                if (lines.isNotEmpty()) {
                    val contacts = sociusJsonToContacts(lines)
                    importContacts(contacts)
                }
            } catch (th: IOException) {
                Log.e("ManagePage", "Unable to read import file", th)
            }
            isImporting = false
            onDismiss()
        }
    }

    var selectedOption: ImportExportOption by remember { mutableStateOf(ImportExportOption.None) }

    Dialog(
        onDismissRequest = {
            if (isImporting) return@Dialog
            onDismiss()
        },
    ) {
        Card {
            Column (
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(16.dp),
            ) {
                if (!isImporting) {
                    Text(stringResource(R.string.import_dialog_title))
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        ElevatedButton(
                            onClick = onDismiss
                        ) {
                            Text(stringResource(R.string.cancel))
                        }
                        Spacer(Modifier.weight(1f))
                        if (selectedOption != ImportExportOption.None) {
                            Button(
                                onClick = {
                                    when (selectedOption) {
                                        ImportExportOption.None -> {
                                            onDismiss()
                                        }

                                        ImportExportOption.SociusJson -> {
                                            sociusJsonLauncher.launch(arrayOf("application/json"))
                                            isImporting = true
                                        }

                                        ImportExportOption.GoogleCsv -> {
                                            googleCsvLauncher.launch(arrayOf("text/comma-separated-values"))
                                            isImporting = true
                                        }
                                    }
                                }
                            ) {
                                Text(stringResource(R.string.next))
                            }
                        }
                    }
                } else {
                    CircularProgressIndicator()
                }
            }
        }
    }
}