package de.benkralex.socius.widgets.settings

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import de.benkralex.socius.data.settings.dateFormat
import de.benkralex.socius.data.settings.saveSettings

class DateFormattingState (
    private val context: Context
) {
    var selectedFormat by mutableIntStateOf(dateFormat)

    fun changeSetting(format: Int) {
        selectedFormat = format
        dateFormat = format
        saveSettings(context)
    }
}