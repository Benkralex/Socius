package de.benkralex.socius.pages

import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import de.benkralex.socius.data.contacts.contacts
import de.benkralex.socius.data.contacts.groups
import de.benkralex.socius.data.contacts.loadContacts
import de.benkralex.socius.data.contacts.loadingContacts
import de.benkralex.socius.data.settings.getFormattedName

class ContactsListViewModel : ViewModel() {
    var searchQuery by mutableStateOf("")
        private set
    var selectedGroupsFilter by mutableStateOf(emptySet<String>())
        private set

    val unselectedGroupsFilter by derivedStateOf {
        groups.filter { !it.name.isNullOrBlank() }.map { it.name!! }.toSet() - selectedGroupsFilter
    }

    val filteredContacts by derivedStateOf {
        val searchQueryFiltered = if (searchQuery.isNotBlank()) {
            contacts.filter {
                getFormattedName(it).contains(searchQuery, ignoreCase = true)
            }
        } else {
            contacts
        }

        val groupsFiltered = if (selectedGroupsFilter.isNotEmpty()) {
            searchQueryFiltered.filter { contact ->
                selectedGroupsFilter.all { group ->
                    group in contact.groups.filter { !it.name.isNullOrBlank() }.map { it.name!! }
                }
            }
        } else {
            searchQueryFiltered
        }

        groupsFiltered
    }

    val grouped by derivedStateOf {
        filteredContacts.groupBy {
            if (it.isStarred) "starred"
            else getFormattedName(it).firstOrNull()?.uppercase() ?: "?"
        }.toSortedMap(compareBy {
            when (it) {
                "starred" -> "A"
                "?" -> "AA"
                else -> it + "B"
            }
        })
    }

    val showNoResultsMsg by derivedStateOf {
        grouped.isEmpty() && contacts.isNotEmpty()
    }

    val isRefreshing by derivedStateOf {
        loadingContacts
    }

    val willRefresh by derivedStateOf {
        (pullToRefreshState?.distanceFraction ?: 0f) > 1f
    }

    var pullToRefreshState: PullToRefreshState? = null

    fun onSearchQueryChange(newQuery: String) {
        searchQuery = newQuery
    }

    fun addGroupFilter(group: String) {
        selectedGroupsFilter = selectedGroupsFilter + group
    }

    fun removeGroupFilter(group: String) {
        selectedGroupsFilter = selectedGroupsFilter - group
    }

    fun refreshContacts() {
        loadContacts()
    }
}