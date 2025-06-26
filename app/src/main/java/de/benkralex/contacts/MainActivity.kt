package de.benkralex.contacts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.content.MediaType.Companion.Image
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedCard
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
            var selectedItem by remember { mutableIntStateOf(0) }
            val items = listOf(
                stringResource(R.string.menu_bar_contacts),
                stringResource(R.string.menu_bar_highlights),
                stringResource(R.string.menu_bar_manage)
            )
            val selectedIcons = listOf(
                Icons.Filled.Person,
                Icons.Filled.Favorite,
                Icons.Filled.Build
            )
            val unselectedIcons = listOf(
                Icons.Outlined.Person,
                Icons.Outlined.FavoriteBorder,
                Icons.Outlined.Build
            )

            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
                                contentDescription = item,
                            )
                        },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                    )
                }
            }
        }
    ) { paddingValues ->
        var contacts = listOf(
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
        ).sorted()

        val grouped = contacts.groupBy { it[0] }

        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxWidth()
        ) {
            grouped.forEach { (initial, contactsForInitial) ->
                stickyHeader {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0, 0, 0, 1),
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = initial.toString(),
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                            style = TextStyle(
                                fontWeight = FontWeight.Bold
                            )                        )
                    }
                }

                items(contactsForInitial) { c ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0, 0, 0, 1),
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.profile_picture), // oder eine dynamische ID
                                contentDescription = null,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = c)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ContactsTheme {
        Greeting()
    }
}