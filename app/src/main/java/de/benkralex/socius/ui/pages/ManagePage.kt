package de.benkralex.socius.ui.pages

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.Input
import androidx.compose.material.icons.outlined.ImportExport
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.benkralex.socius.R
import de.benkralex.socius.data.contacts.contacts
import de.benkralex.socius.ui.import_export.ExportDialog
import de.benkralex.socius.ui.import_export.ImportDialog
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
            },
        )
    }
    if (showExportFormatSelectionDialog) {
        ExportDialog(
            onDismiss = {
                showExportFormatSelectionDialog = false
            },
            contacts = contacts,
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