package de.benkralex.socius.ui.components.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class BooleanSettingState (
    val onChangeCallback: (Boolean) -> Unit,
    initialValue: Boolean,
) {
    var value by mutableStateOf(initialValue)

    fun onChange(b: Boolean) {
        value = b
        onChangeCallback(b)
    }
}