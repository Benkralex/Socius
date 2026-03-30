package de.benkralex.socius.ui.components.contactsList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.benkralex.socius.R
import de.benkralex.socius.ui.pages.ContactListPageViewModel

@Composable
fun SearchBar(
    viewModel: ContactListPageViewModel,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = viewModel.searchQuery,
        onValueChange = { viewModel.onSearchQueryChange(it) },
        modifier = modifier
            .padding(8.dp)
            .padding(horizontal = 12.dp)
            .fillMaxWidth(),
        placeholder = {
            Text(
                text = stringResource(R.string.searchbar_placeholder),
            )
        },
        singleLine = true,
        leadingIcon = {
            Icon(
                modifier = Modifier
                    .padding(start = 8.dp),
                imageVector = Icons.Filled.Search,
                contentDescription = null,
            )
        },
        trailingIcon = {
            if (viewModel.searchQuery.isNotEmpty()) {
                Icon(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable { viewModel.onSearchQueryChange("") },
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(R.string.content_desc_clear_search)
                )
            }
        },
        shape = RoundedCornerShape(32.dp),
    )
}