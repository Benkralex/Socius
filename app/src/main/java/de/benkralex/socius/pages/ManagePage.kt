package de.benkralex.socius.pages

import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.Input
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import de.benkralex.socius.data.import_export.googleCsvToContacts
import de.benkralex.socius.data.import_export.importContacts
import de.benkralex.socius.data.import_export.vCardToContacts
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
    val context = LocalContext.current
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
        }
    }
    val vCardLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri == null || activity == null) return@rememberLauncherForActivityResult
        scope.launch(Dispatchers.IO) {
            try {
                val lines = getLinesOfFile(uri, activity)
                if (lines.isNotEmpty()) {
                    importContacts(vCardToContacts(lines))
                }
            } catch (th: IOException) {
                Log.e("ManagePage", "Unable to read import file", th)
            }
        }
    }

    var importDialogOpen by remember { mutableStateOf(false) }

    AnimatedVisibility(importDialogOpen) {
        Dialog(
            onDismissRequest = {
                importDialogOpen = false
            }
        ) {
            Column {
                Button(
                    onClick = {
                        googleCsvLauncher.launch(arrayOf("text/csv"))
                        importDialogOpen = false
                    }
                ) {
                    Text("Google CSV")
                }
                Button(
                    onClick = {
                        vCardLauncher.launch(arrayOf("text/vcard", "text/x-vcard", "text/directory;profile=vCard", "text/directory"))
                        importDialogOpen = false
                    }
                ) {
                    Text("VCard")
                }
            }
        }
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
                            importDialogOpen = true
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
        }
    }
}