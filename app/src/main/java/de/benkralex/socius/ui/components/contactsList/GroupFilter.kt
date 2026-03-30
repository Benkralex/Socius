package de.benkralex.socius.ui.components.contactsList

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.benkralex.socius.R
import de.benkralex.socius.ui.pages.ContactListPageViewModel

@Composable
fun GroupFilter (
    viewModel: ContactListPageViewModel,
    modifier: Modifier = Modifier,
) {
    LazyRow (
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        item {
            Spacer(Modifier.width(16.dp))
        }
        items(viewModel.selectedGroupsFilter.toList()) {
            FilterChip(
                selected = true,
                onClick = { viewModel.removeGroupFilter(it) },
                label = { Text(it) },
                modifier = Modifier
                    .padding(end = 8.dp),
                shape = RoundedCornerShape(32.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = stringResource(R.string.content_desc_selected),
                    )
                }
            )
        }
        items(viewModel.unselectedGroupsFilter.toList()) {
            FilterChip(
                selected = false,
                onClick = { viewModel.addGroupFilter(it) },
                label = { Text(it) },
                modifier = Modifier
                    .padding(end = 8.dp),
            )
        }
        item {
            Spacer(Modifier.width(16.dp))
        }
    }
}