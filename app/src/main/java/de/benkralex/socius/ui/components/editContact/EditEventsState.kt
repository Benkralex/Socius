package de.benkralex.socius.ui.components.editContact

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import de.benkralex.socius.data.model.Contact
import de.benkralex.socius.data.model.Event
import de.benkralex.socius.data.model.Type
import java.util.Calendar

class EditEventsState {
    val showFields by derivedStateOf { count > 0 }
    var count: Int by mutableIntStateOf(0)
    var dates: MutableList<MutableState<Long?>> by mutableStateOf(mutableListOf())
    var types: MutableList<MutableState<Type.Event>> by mutableStateOf(mutableListOf())

    fun hasRelevantData(): Boolean {
        return dates.any { it.value != null }
    }

    fun isRelevant(i: Int): Boolean {
        return dates[i].value != null
    }
    
    fun getRelevantData(): List<Event> {
        val events: MutableList<Event> = mutableListOf()
        for (i in 0..<count) {
            if (isRelevant(i)) {
                val calendar = Calendar.getInstance().apply { timeInMillis = dates[i].value!! }

                events.add(
                    Event(
                        day = calendar.get(Calendar.DAY_OF_MONTH),
                        month = calendar.get(Calendar.MONTH) + 1,
                        year = calendar.get(Calendar.YEAR),
                        type = types[i].value,
                    )
                )
            }
        }
        return events
    }

    fun addNew() {
        dates.add(mutableStateOf(null))
        if (types.none { it.value == Type.Event.BIRTHDAY })
            types.add(mutableStateOf(Type.Event.BIRTHDAY))
        else
            types.add(mutableStateOf(Type.Event.ANNIVERSARY))
        count++
    }

    fun loadFromContact(contact: Contact) {
        for (event: Event in contact.events) {
            val calendar = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_MONTH, event.day)
                set(Calendar.MONTH, event.month - 1)
                set(Calendar.YEAR, event.year ?: Calendar.getInstance().get(Calendar.YEAR))
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            count++
            dates.add(mutableStateOf(calendar.timeInMillis))
            types.add(mutableStateOf(event.type))
        }
    }

    fun reset() {
        count = 0
        dates.clear()
        types.clear()
    }
}