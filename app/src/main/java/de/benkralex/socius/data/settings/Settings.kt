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
var fullNameFormat: Int = 0
var preferNickname by mutableStateOf(true)
var dateFormat: Int = 0

val charsToTrim: CharArray = charArrayOf(',', ' ', '/', '\\', '.', '-', '_', ' ')

fun saveSettings(context: Context) {
    val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    sharedPreferences.edit {
        putInt("nameFormat", nameFormat)
        putInt("fullNameFormat", fullNameFormat)
        putBoolean("preferNickname", preferNickname)
        putInt("dateFormat", dateFormat)
    }
}

fun loadSettings(context: Context) {
    val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    nameFormat = sharedPreferences.getInt("nameFormat", 0)
    fullNameFormat = sharedPreferences.getInt("fullNameFormat", 0)
    preferNickname = sharedPreferences.getBoolean("preferNickname", true)
    dateFormat = sharedPreferences.getInt("dateFormat", 0)
}