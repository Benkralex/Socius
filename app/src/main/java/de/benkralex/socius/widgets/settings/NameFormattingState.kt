package de.benkralex.socius.widgets.settings

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import de.benkralex.socius.data.settings.nameFormat
import de.benkralex.socius.data.settings.saveSettings

class NameFormattingState (
    private val context: Context
) {
    var selectedFormat by mutableIntStateOf(nameFormat)

    fun changeSetting(format: Int) {
        selectedFormat = format
        nameFormat = format
        saveSettings(context)
    }
}