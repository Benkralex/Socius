package de.benkralex.socius

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation3.runtime.NavBackStack
import de.benkralex.socius.data.contacts.system.load.loadFromURI
import de.benkralex.socius.navigation.ContactDetailIntentNavKey
import de.benkralex.socius.navigation.NavigationRoot
import de.benkralex.socius.navigation.backStack
import de.benkralex.socius.theme.ContactsTheme

class MainActivity : ComponentActivity() {
    companion object {
        lateinit var instance: MainActivity
            private set
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        instance = this

        setContent {
            ContactsTheme {
                NavigationRoot(Modifier)
            }
            LaunchedEffect(Unit) {
                handleIntent(intent, backStack, this@MainActivity)
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
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.let {}
    }

    private fun handleIntent(intent: Intent, backStack: NavBackStack, ctx: Context) {
        if (Intent.ACTION_VIEW == intent.action) {
            val contactUri = intent.data
            contactUri?.let { uri ->
                Log.d("Open Contact", "Received intent to view contact: $uri")
                val contact = loadFromURI(ctx, uri)
                //uriLoadedContacts = listOf(contact)
                backStack.add(ContactDetailIntentNavKey(contact.id))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContactsListPagePreview() {
    ContactsTheme {
        NavigationRoot(Modifier)
    }
}