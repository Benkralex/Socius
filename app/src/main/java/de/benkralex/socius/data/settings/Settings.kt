package de.benkralex.socius.data.settings

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.edit

data class Format(
    val displayNameResource: Int,
    val format: String,
    val id: Int,
)

var nameFormat: Int = 0
var dateFormat: Int = 0
var loadAndroidSystemContacts by mutableStateOf(true)

val charsToTrim: CharArray = charArrayOf(',', ' ', '/', '\\', '.', '-', '_', ' ')

fun saveSettings(context: Context) {
    val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    sharedPreferences.edit {
        putInt("nameFormat", nameFormat)
        putInt("dateFormat", dateFormat)
        putBoolean("loadAndroidSystemContacts", loadAndroidSystemContacts)
    }
}

fun loadSettings(context: Context) {
    val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    nameFormat = sharedPreferences.getInt("nameFormat", 0)
    dateFormat = sharedPreferences.getInt("dateFormat", 0)
    loadAndroidSystemContacts = sharedPreferences.getBoolean("loadAndroidSystemContacts", true)
}