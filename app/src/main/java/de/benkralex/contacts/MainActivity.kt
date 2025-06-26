package de.benkralex.contacts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.benkralex.contacts.ui.theme.ContactsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ContactsTheme {
                Greeting()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun Greeting() {
    Scaffold(
        topBar = {
            TopAppBar(title = {Text("PLACEHOLDER SEARCHBAR")})
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* do something */ }) {
                Icon(Icons.Filled.Add, "Add")
            }
        },
        bottomBar = {
            CustomNavigationBar (
                items = listOf(
                    R.string.menu_bar_contacts,
                    R.string.menu_bar_highlights,
                    R.string.menu_bar_manage
                ),
                selectedIcons = listOf(
                    Icons.Filled.Person,
                    Icons.Filled.Favorite,
                    Icons.Filled.Build
                ),
                unselectedIcons = listOf(
                    Icons.Outlined.Person,
                    Icons.Outlined.FavoriteBorder,
                    Icons.Outlined.Build
                ),
            )
        }
    ) { paddingValues ->
        val grouped = listOf(
            "Lena Schröder",
            "Maximilian Weber",
            "Sofia Richter",
            "Jonas Meier",
            "Emily Schneider",
            "Felix Hoffmann",
            "Laura Wagner",
            "Niklas Becker",
            "Anna Scholz",
            "Tim Schäfer",
            "Mia Krämer",
            "Ben Zimmermann",
            "Lea Hartmann",
            "Noah Lorenz",
            "Pauline Busch",
            "Luca Frank",
            "Marie Neumann",
            "Elias König",
            "Johanna Peters",
            "David Schuster"
        ).sorted().groupBy { it[0] }

        ContactsList(
            grouped = grouped,
            paddingValues = paddingValues
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ContactsTheme {
        Greeting()
    }
}