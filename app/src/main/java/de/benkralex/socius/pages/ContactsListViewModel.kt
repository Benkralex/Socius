package de.benkralex.socius.pages

import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import de.benkralex.socius.data.contacts.contacts
import de.benkralex.socius.data.contacts.groups
import de.benkralex.socius.data.contacts.loadAllContacts
import de.benkralex.socius.data.contacts.loadingContacts
import de.benkralex.socius.data.settings.getFormattedName
import java.util.Locale.getDefault

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
            contacts.filter { contact ->
                getFormattedName(contact).contains(searchQuery, ignoreCase = true) ||
                        contact.emails.any {it.address.contains(searchQuery, ignoreCase = true)} ||
                        contact.phoneNumbers.any {it.number.contains(searchQuery, ignoreCase = true)} ||
                        contact.websites.any {it.url.contains(searchQuery, ignoreCase = true)} ||
                        contact.note?.contains(searchQuery, ignoreCase = true) ?: false ||
                        contact.prefix?.contains(searchQuery, ignoreCase = true) ?: false ||
                        contact.givenName?.contains(searchQuery, ignoreCase = true) ?: false ||
                        contact.middleName?.contains(searchQuery, ignoreCase = true) ?: false ||
                        contact.familyName?.contains(searchQuery, ignoreCase = true) ?: false ||
                        contact.suffix?.contains(searchQuery, ignoreCase = true) ?: false ||
                        contact.nickname?.contains(searchQuery, ignoreCase = true) ?: false
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
        filteredContacts.sortedBy {
            getFormattedName(it)
                .uppercase(getDefault())
        }.groupBy {
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

    val showNoContactsMsg by derivedStateOf {
        contacts.isEmpty()
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
        loadAllContacts()
    }
}