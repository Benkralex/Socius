package de.benkralex.socius.pages

import android.content.Context
import android.net.Uri
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.Input
import androidx.compose.material.icons.outlined.ImportExport
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import de.benkralex.socius.data.contacts.contacts
import de.benkralex.socius.data.import_export.contactsToGoogleCsv
import de.benkralex.socius.data.import_export.contactsToSociusJson
import de.benkralex.socius.data.import_export.exportContacts
import de.benkralex.socius.data.import_export.googleCsvToContacts
import de.benkralex.socius.data.import_export.importContacts
import de.benkralex.socius.data.import_export.sociusJsonToContacts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException

@Throws(IOException::class)
fun getLinesOfFile(uri: Uri, activity: ComponentActivity): List<String> {
    val resolver = activity.contentResolver
    val stream = resolver.openInputStream(uri) ?: return emptyList()
    val lines = stream.bufferedReader().use(BufferedReader::readLines)
    val newLines: MutableList<String> = mutableListOf()
    var c = 0
    for (l in lines) {
        if (c % 2 == 0) {
            newLines.add(l)
        } else {
            newLines[newLines.size - 1] += "\n" + l
        }
        c += l.count { it == '"' }
    }
    return newLines
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagePage(
    modifier: Modifier = Modifier,
    menuBar: @Composable () -> Unit,
    navigateSettings: () -> Unit = {},
) {
    var showImportFormatSelectionDialog by remember { mutableStateOf(false) }
    var showExportFormatSelectionDialog by remember { mutableStateOf(false) }

    if (showImportFormatSelectionDialog) {
        ImportDialog(
            onDismiss = {
                showImportFormatSelectionDialog = false
            }
        )
    }
    if (showExportFormatSelectionDialog) {
        ExportDialog(
            onDismiss = {
                showExportFormatSelectionDialog = false
            }
        )
    }

    Scaffold (
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.page_manage))
                }
            )
        },
        bottomBar = {
            menuBar()
        }
    ) { paddingValues ->
        Column (
            modifier = Modifier
                .padding(paddingValues)
                .padding(8.dp)
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable(
                        onClick = navigateSettings
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.CenterVertically),
                )
                Text(
                    text = stringResource(R.string.page_settings),
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.CenterVertically),
                )
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable(
                        onClick = {
                            showImportFormatSelectionDialog = true
                        }
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Input,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.CenterVertically),
                )
                Text(
                    text = stringResource(R.string.import_contacts),
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                )
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable(
                        onClick = {
                            showExportFormatSelectionDialog = true
                        }
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Outlined.ImportExport,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.CenterVertically),
                )
                Text(
                    text = stringResource(R.string.export_contacts),
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                )
            }
        }
    }
}

@Composable
fun ExportDialog (
    onDismiss: () -> Unit = {},
    context: Context = LocalContext.current,
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    ElevatedButton (
                        onClick = onDismiss
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                    Spacer(Modifier.weight(1f))
                    if (selectedOption != ImportExportOption.None) {
                        Button (
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
                            }
                        ) {
                            Text(stringResource(R.string.next))
                        }
                    }
                }
            }
        }
    }
}

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

enum class ImportExportOption {
    None,
    GoogleCsv,
    SociusJson,
}