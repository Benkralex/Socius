package de.benkralex.socius

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import de.benkralex.socius.data.contacts.local.database.LocalContactsDao
import de.benkralex.socius.data.contacts.local.database.LocalContactsDatabase
import de.benkralex.socius.data.settings.noName
import de.benkralex.socius.data.syncadapter.SyncManager
import de.benkralex.socius.ui.navigation.NavigationRoot
import de.benkralex.socius.ui.theme.ContactsTheme

class MainActivity : ComponentActivity() {
    companion object {
        lateinit var instance: MainActivity
            private set

        lateinit var localContactsDatabase: LocalContactsDatabase
            private set

        lateinit var localContactsDao: LocalContactsDao
            private set
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        instance = this
        localContactsDatabase = LocalContactsDatabase.getDatabase(this)
        localContactsDao = localContactsDatabase.localContactsDao()

        setContent {
            noName = stringResource(R.string.contact_no_name)
            ContactsTheme {
                NavigationRoot(Modifier)
            }
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS
                ),
                1
            )
        } else {
            // Initialize sync account and trigger initial sync when permissions are granted
            initializeSyncAdapter()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            // Initialize sync after permissions are granted
            initializeSyncAdapter()
        }
    }

    private fun initializeSyncAdapter() {
        // Create the sync account if it doesn't exist
        SyncManager.getOrCreateSyncAccount(this)
        // Request an initial sync
        SyncManager.requestSync(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.let {}
    }
}

@Preview(showBackground = true)
@Composable
fun ContactsListPagePreview() {
    ContactsTheme {
        NavigationRoot(Modifier)
    }
}