package de.benkralex.socius.backend.settings

import android.content.Context

data class Format(
    val displayNameResource: Int,
    val format: String,
    val id: Int,
)

var nameFormat: Int = 0
var dateFormat: Int = 0

val charsToTrim: CharArray = charArrayOf(',', ' ', '/', '\\', '.', '-', '_', ' ')

fun saveSettings(context: Context) {
    val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        putString("nameFormat", nameFormat.toString())
        putString("dateFormat", dateFormat.toString())
        apply()
    }
}

fun loadSettings(context: Context) {
    val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    nameFormat = try {
        sharedPreferences.getString("nameFormat", "0")?.toInt() ?: 0
    } catch (e: NumberFormatException) {
        0 // Default to first format if parsing fails
    }
    dateFormat = try {
        sharedPreferences.getString("dateFormat", "0")?.toInt() ?: 0
    } catch (e: NumberFormatException) {
        0 // Default to first format if parsing fails
    }
}