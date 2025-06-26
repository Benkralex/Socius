package de.benkralex.contacts

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

/*class CustomNavigationBar {
    val items: List<Int>
    val selectedIcons: List<ImageVector>
    val unselectedIcons: List<ImageVector>
    val selectedIndex: Int

    constructor(
        items: List<Int>,
        selectedIcons: List<ImageVector>,
        unselectedIcons: List<ImageVector>
    ) {
        this.items = items
        this.selectedIcons = selectedIcons
        this.unselectedIcons = unselectedIcons
        this.selectedIndex = 0
    }

    @Composable
    fun GetNavigationBar(index: Int): Unit {
        var selectedItem by remember { mutableIntStateOf(0) }
        NavigationBar {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
                            contentDescription = stringResource(item),
                        )
                    },
                    label = { Text(stringResource(item)) },
                    selected = selectedItem == index,
                    onClick = { selectedItem = index },
                )
            }
        }
    }

}*/

@Composable
fun CustomNavigationBar(
    items: List<Int>,
    selectedIcons: List<ImageVector>,
    unselectedIcons: List<ImageVector>
) {
    var selectedItem by remember { mutableIntStateOf(0) }
    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
                        contentDescription = stringResource(item),
                    )
                },
                label = { Text(stringResource(item)) },
                selected = selectedItem == index,
                onClick = { selectedItem = index },
            )
        }
    }
}