package de.benkralex.socius.data.import_export.google_csv

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.graphics.createBitmap
import android.util.Log
import de.benkralex.socius.data.model.Address
import de.benkralex.socius.data.model.Contact
import de.benkralex.socius.data.model.ContactOrigin
import de.benkralex.socius.data.model.Email
import de.benkralex.socius.data.model.Event
import de.benkralex.socius.data.model.Phone
import de.benkralex.socius.data.model.Relation
import de.benkralex.socius.data.model.Type
import de.benkralex.socius.data.model.Website
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.min

fun parseCSVLine(line: String): List<String> {
    val result = mutableListOf<String>()
    var current = StringBuilder()
    var insideQuotes = false
    var i = 0
    while (i < line.length) {
        when (val char = line[i]) {
            '"' -> {
                insideQuotes = !insideQuotes
                i++
            }
            ',' if !insideQuotes -> {
                result.add(current.toString())
                current = StringBuilder()
                i++
            }
            else -> {
                current.append(char)
                i++
            }
        }
    }
    result.add(current.toString())
    return result
}
suspend fun loadPicture(link: String): Bitmap = withContext(Dispatchers.IO) {
    try {
        val connection = (URL(link).openConnection() as HttpURLConnection).apply {
            connectTimeout = 10_000
            readTimeout = 10_000
            requestMethod = "GET"
            doInput = true
            connect()
        }

        try {
            connection.inputStream.use { input ->
                BitmapFactory.decodeStream(input) ?: createBitmap(1, 1)
            }
        } finally {
            connection.disconnect()
        }
    } catch (e: Exception) {
        Log.e("GoogleCsvImport", "Bild konnte nicht geladen werden: $link", e)
        createBitmap(1, 1)
    }
}

suspend fun googleCsvToContacts(file: List<String>): List<Contact> {
    val contacts: MutableList<Contact> = mutableListOf()
    val cols: List<String> = file[0].split(",")
    for (i in 1..<file.size) {
        val line = file[i]
        val values: List<String> = parseCSVLine(line)

        val contact = Contact(
            id = null,
            origin = ContactOrigin.TEMPORARY,
        )
        val emailLabels: MutableMap<Int, String> = mutableMapOf()
        val emailAddresses: MutableMap<Int, String> = mutableMapOf()

        val phoneLabels: MutableMap<Int, String> = mutableMapOf()
        val phoneNumbers: MutableMap<Int, String> = mutableMapOf()

        val websiteLabels: MutableMap<Int, String> = mutableMapOf()
        val websiteURLs: MutableMap<Int, String> = mutableMapOf()

        val relationLabels: MutableMap<Int, String> = mutableMapOf()
        val relationNames: MutableMap<Int, String> = mutableMapOf()

        val customLabels: MutableMap<Int, String> = mutableMapOf()
        val customValues: MutableMap<Int, String> = mutableMapOf()

        val eventLabels: MutableMap<Int, String> = mutableMapOf()
        val eventDates: MutableMap<Int, String> = mutableMapOf()

        val addressLabels: MutableMap<Int, String> = mutableMapOf()
        val addressStreets: MutableMap<Int, String> = mutableMapOf()
        val addressCities: MutableMap<Int, String> = mutableMapOf()
        val addressRegions: MutableMap<Int, String> = mutableMapOf()
        val addressPostcodes: MutableMap<Int, String> = mutableMapOf()
        val addressCountries: MutableMap<Int, String> = mutableMapOf()
        val addressPOBoxes: MutableMap<Int, String> = mutableMapOf()

        for (j in 0..<min(cols.size, values.size)) {
            val col: String = cols[j]
            val value: String = values[j]
            when {
                col == "First Name" -> {
                    if (value.isEmpty()) continue
                    contact.name.firstname = value
                }
                col == "Middle Name" -> {
                    if (value.isEmpty()) continue
                    contact.name.secondName = value
                }
                col == "Last Name" -> {
                    if (value.isEmpty()) continue
                    contact.name.lastname = value
                }
                col == "Phonetic First Name" -> {
                    if (value.isEmpty()) continue
                    contact.name.phoneticFirstname = value
                }
                col == "Phonetic Middle Name" -> {
                    if (value.isEmpty()) continue
                    contact.name.phoneticSecondName = value
                }
                col == "Phonetic Last Name" -> {
                    if (value.isEmpty()) continue
                    contact.name.phoneticLastname = value
                }
                col == "Name Prefix" -> {
                    if (value.isEmpty()) continue
                    contact.name.prefix = value
                }
                col == "Name Suffix" -> {
                    if (value.isEmpty()) continue
                    contact.name.suffix = value
                }
                col == "Nickname" -> {
                    if (value.isEmpty()) continue
                    contact.name.nickname = value
                }
                col == "File As" -> {
                    if (value.isEmpty()) continue
                    contact.name.displayName = value
                }
                col == "Organization Name" -> {
                    if (value.isEmpty()) continue
                    contact.job.organization = value
                }
                col == "Organization Title" -> {
                    if (value.isEmpty()) continue
                    contact.job.jobTitle = value
                }
                col == "Organization Department" -> {
                    if (value.isEmpty()) continue
                    contact.job.department = value
                }
                col == "Birthday" -> {
                    if (value.isEmpty()) continue
                    val date = value.trim().replace("-", "")
                    try {
                        when (date.length) {
                            8 -> {
                                contact.events += Event(
                                    year = date.take(4).toIntOrNull(),
                                    month = date.substring(4, 6).toInt(),
                                    day = date.substring(6, 8).toInt(),
                                    type = Type.Event.BIRTHDAY,
                                )
                            }

                            4 -> {
                                contact.events += Event(
                                    month = date.take(2).toInt(),
                                    day = date.substring(2, 4).toInt(),
                                    type = Type.Event.BIRTHDAY,
                                )
                            }

                            else -> {
                                throw IllegalArgumentException("Invalid date format: $value")
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("GoogleCsvImport", "Ungültiges Datum: $value", e)
                    }
                }
                col == "Notes" -> {
                    if (value.isEmpty()) continue
                    contact.note = value
                }
                col == "Photo" -> {
                    if (value.isEmpty()) continue
                    contact.profilePicture.bitmap = loadPicture(value)
                }
                col == "Labels" -> {
                    if (value.isEmpty()) continue
                    value.split(" ::: ").forEach {
                        val grpName = it.trim(' ', '*')
                        if (grpName == "starred") {
                            contact.isStarred = true
                        } else if (grpName != "myContacts") {
                            contact.groups += grpName
                        }
                    }
                }
                col.startsWith("E-mail ") -> {
                    if (col.endsWith(" - Label")) {
                        val mailIndex = col.substring(7, col.length - 8).toInt()
                        emailLabels[mailIndex] = value
                    } else if (col.endsWith(" - Value")) {
                        val mailIndex = col.substring(7, col.length - 8).toInt()
                        if (value.isEmpty()) continue
                        emailAddresses[mailIndex] = value
                    }
                }
                col.startsWith("Phone ") -> {
                    if (col.endsWith(" - Label")) {
                        val phoneIndex = col.substring(6, col.length - 8).toInt()
                        phoneLabels[phoneIndex] = value
                    } else if (col.endsWith(" - Value")) {
                        val phoneIndex = col.substring(6, col.length - 8).toInt()
                        if (value.isEmpty()) continue
                        phoneNumbers[phoneIndex] = value
                    }
                }
                col.startsWith("Relation ") -> {
                    if (col.endsWith(" - Label")) {
                        val relationIndex = col.substring(9, col.length - 8).toInt()
                        relationLabels[relationIndex] = value
                    } else if (col.endsWith(" - Value")) {
                        val relationIndex = col.substring(9, col.length - 8).toInt()
                        if (value.isEmpty()) continue
                        relationNames[relationIndex] = value
                    }
                }
                col.startsWith("Website ") -> {
                    if (col.endsWith(" - Label")) {
                        val websiteIndex = col.substring(8, col.length - 8).toInt()
                        websiteLabels[websiteIndex] = value
                    } else if (col.endsWith(" - Value")) {
                        val websiteIndex = col.substring(8, col.length - 8).toInt()
                        if (value.isEmpty()) continue
                        websiteURLs[websiteIndex] = value
                    }
                }
                col.startsWith("Custom Field ") -> {
                    if (col.endsWith(" - Label")) {
                        val customIndex = col.substring(13, col.length - 8).toInt()
                        customLabels[customIndex] = value
                    } else if (col.endsWith(" - Value")) {
                        val customIndex = col.substring(13, col.length - 8).toInt()
                        if (value.isEmpty()) continue
                        customValues[customIndex] = value
                    }
                }
                col.startsWith("Event ") -> {
                    if (col.endsWith(" - Label")) {
                        val eventIndex = col.substring(6, col.length - 8).toInt()
                        eventLabels[eventIndex] = value
                    } else if (col.endsWith(" - Value")) {
                        val eventIndex = col.substring(6, col.length - 8).toInt()
                        if (value.isEmpty()) continue
                        eventDates[eventIndex] = value
                    }
                }
                col.startsWith("Address ") -> {
                    if (col.endsWith(" - Label")) {
                        val addressIndex = col.substring(8).split("-")[0].trim().toInt()
                        addressLabels[addressIndex] = value
                    } else if (col.endsWith(" - Street")) {
                        val addressIndex = col.substring(8).split("-")[0].trim().toInt()
                        if (value.isEmpty()) continue
                        addressStreets[addressIndex] = value
                    } else if (col.endsWith(" - City")) {
                        val addressIndex = col.substring(8).split("-")[0].trim().toInt()
                        if (value.isEmpty()) continue
                        addressCities[addressIndex] = value
                    } else if (col.endsWith(" - Region")) {
                        val addressIndex = col.substring(8).split("-")[0].trim().toInt()
                        if (value.isEmpty()) continue
                        addressRegions[addressIndex] = value
                    } else if (col.endsWith(" - Postal Code")) {
                        val addressIndex = col.substring(8).split("-")[0].trim().toInt()
                        if (value.isEmpty()) continue
                        addressPostcodes[addressIndex] = value
                    } else if (col.endsWith(" - Country")) {
                        val addressIndex = col.substring(8).split("-")[0].trim().toInt()
                        if (value.isEmpty()) continue
                        addressCountries[addressIndex] = value
                    } else if (col.endsWith(" - PO Box")) {
                        val addressIndex = col.substring(8).split("-")[0].trim().toInt()
                        if (value.isEmpty()) continue
                        addressPOBoxes[addressIndex] = value
                    }
                }
                /*
                "Address 1 - Formatted"
                "Address 1 - Extended Address"
                */
            }
        }
        for ((k, v) in emailLabels) {
            if (emailAddresses.containsKey(k) && !emailAddresses[k].isNullOrBlank()) {
                val addresses = emailAddresses[k]!!.split(" ::: ")
                val label = v.trim(' ', '*')
                for (add in addresses) {
                    contact.emails += Email(
                        value = add,
                        type = Type.convertEmailType(label),
                    )
                }
            }
        }
        for ((k, v) in phoneLabels) {
            if (phoneNumbers.containsKey(k) && !phoneNumbers[k].isNullOrBlank()) {
                val numbers = phoneNumbers[k]!!.split(" ::: ")
                val label = v.trim(' ', '*')
                for (num in numbers) {
                    contact.phoneNumbers += Phone(
                        value = num,
                        type = Type.convertPhoneType(label),
                    )
                }
            }
        }
        for ((k, v) in relationLabels) {
            if (relationNames.containsKey(k) && !relationNames[k].isNullOrBlank()) {
                val names = relationNames[k]!!.split(" ::: ")
                val label = v.trim(' ', '*')
                for (name in names) {
                    contact.relations += Relation(
                        value = name,
                        type =Type.convertRelationType(label),
                    )
                }
            }
        }
        for ((k, v) in websiteLabels) {
            if (websiteURLs.containsKey(k) && !websiteURLs[k].isNullOrBlank()) {
                val urls = websiteURLs[k]!!.split(" ::: ")
                val label = v.trim(' ', '*')
                for (url in urls) {
                    contact.websites += Website(
                        value = url,
                        type = Type.convertWebsiteType(label),
                    )
                }
            }
        }
        for ((k, v) in customLabels) {
            if (customValues.containsKey(k) && !customValues[k].isNullOrBlank()) {
                val values = customValues[k]!!.split(" ::: ")
                val label = v.trim(' ', '*')
                if (values.size == 1 && values[0] == "Display Name") {
                    contact.name.displayName = label
                    continue
                }
                for (value in values) {
                    contact.customFields += Pair(label, value)
                }
            }
        }
        for ((k, v) in eventLabels) {
            if (eventDates.containsKey(k) && !eventDates[k].isNullOrBlank()) {
                val dates = eventDates[k]!!.split(" ::: ").map { it.trim().replace("-", "") }
                val label = v.trim(' ', '*')
                for (date in dates) {
                    try {
                        when (date.length) {
                            8 -> {
                                contact.events += Event(
                                    year = date.take(4).toIntOrNull(),
                                    month = date.substring(4, 6).toInt(),
                                    day = date.substring(6, 8).toInt(),
                                    type = Type.convertEventType(label),
                                )
                            }

                            4 -> {
                                contact.events += Event(
                                    month = date.take(2).toInt(),
                                    day = date.substring(2, 4).toInt(),
                                    type = Type.convertEventType(label),
                                )
                            }

                            else -> {
                                throw IllegalArgumentException("Invalid date format: $date")
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("GoogleCsvImport", "Ungültiges Datum: $date", e)
                    }
                }
            }
        }
        for ((k, v) in addressLabels) {
            val streets = addressStreets[k]?.split(" ::: ")
            val cities = addressCities[k]?.split(" ::: ")
            val regions = addressRegions[k]?.split(" ::: ")
            val postcodes = addressPostcodes[k]?.split(" ::: ")
            val countries = addressCountries[k]?.split(" ::: ")
            val label = v.trim(' ', '*')

            for (l in 0..<listOfNotNull(streets?.size, cities?.size, regions?.size, postcodes?.size, countries?.size, 0).max()) {
                val splitStreet = streets?.get(l)?.split(" ")
                val street =
                    splitStreet?.minus(splitStreet.last { substr -> substr.all { it.isDigit() } })?.joinToString(" ")
                val streetNumber =
                    splitStreet?.last { substr -> substr.all { it.isDigit() } }?.toIntOrNull()
                contact.addresses += Address(
                    street = street,
                    streetNumber = streetNumber,
                    city = cities?.get(l),
                    region = regions?.get(l),
                    postcode = postcodes?.get(l)?.toIntOrNull(),
                    country = countries?.get(l),
                    type = Type.convertAddressType(label),
                )
            }
        }
        contacts.add(contact)
    }
    return contacts
}