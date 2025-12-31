package de.benkralex.socius.widgets.settings

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import de.benkralex.socius.data.settings.fullNameFormat
import de.benkralex.socius.data.settings.saveSettings

class FullNameFormattingState (
    private val context: Context
) {
    var selectedFormat by mutableIntStateOf(fullNameFormat)

    fun changeSetting(format: Int) {
        selectedFormat = format
        fullNameFormat = format
        saveSettings(context)
    }
}