package de.benkralex.socius.data.import_export

import android.graphics.Bitmap
import android.util.Log
import de.benkralex.socius.data.Contact
import de.benkralex.socius.data.ContactEvent
import de.benkralex.socius.data.ContactOrigin
import de.benkralex.socius.data.Email
import de.benkralex.socius.data.Group
import de.benkralex.socius.data.PhoneNumber
import de.benkralex.socius.data.PostalAddress
import de.benkralex.socius.data.Relation
import de.benkralex.socius.data.Website
import java.util.Locale
import java.util.Locale.getDefault
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

fun checkIfLabelIsType(label: String): Boolean {
    return when (label.lowercase()) {
        "home" -> true
        "work" -> true
        "mobile" -> true
        "other" -> true
        "custom" -> true
        else -> false
    }
}

fun loadPicture(link: String): Bitmap {
    val url = java.net.URL(link)
    val connection = url.openConnection() as java.net.HttpURLConnection
    connection.doInput = true
    connection.connect()
    val input = connection.inputStream
    return android.graphics.BitmapFactory.decodeStream(input)
}

fun googleCsvToContacts(file: List<String>): List<Contact> {
    val contacts: MutableList<Contact> = mutableListOf()
    val cols: List<String> = file[0].split(",")
    for (i in 1..<file.size) {
        val line = file[i]
        val values: List<String> = parseCSVLine(line)

        Log.d("Cols", cols.toString())
        Log.d("Values", values.toString())

        val contact = Contact(
            id = "",
            origin = ContactOrigin.IMPORT,
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

        for (j in 0..<min(cols.size, values.size)) {
            val col: String = cols[j]
            val value: String = values[j]
            when {
                col == "First Name" -> {
                    if (value.isEmpty()) continue
                    contact.givenName = value
                }
                col == "Middle Name" -> {
                    if (value.isEmpty()) continue
                    contact.middleName = value
                }
                col == "Last Name" -> {
                    if (value.isEmpty()) continue
                    contact.familyName = value
                }
                col == "Phonetic First Name" -> {
                    if (value.isEmpty()) continue
                    contact.phoneticGivenName = value
                }
                col == "Phonetic Middle Name" -> {
                    if (value.isEmpty()) continue
                    contact.phoneticMiddleName = value
                }
                col == "Phonetic Last Name" -> {
                    if (value.isEmpty()) continue
                    contact.phoneticFamilyName = value
                }
                col == "Name Prefix" -> {
                    if (value.isEmpty()) continue
                    contact.prefix = value
                }
                col == "Name Suffix" -> {
                    if (value.isEmpty()) continue
                    contact.suffix = value
                }
                col == "Nickname" -> {
                    if (value.isEmpty()) continue
                    contact.nickname = value
                }
                col == "File As" -> {
                    if (value.isEmpty()) continue
                    contact.displayName = value
                }
                col == "Organization Name" -> {
                    if (value.isEmpty()) continue
                    contact.organization = value
                }
                col == "Organization Title" -> {
                    if (value.isEmpty()) continue
                    contact.jobTitle = value
                }
                col == "Organization Department" -> {
                    if (value.isEmpty()) continue
                    contact.department = value
                }
                col == "Birthday" -> {
                    if (value.isEmpty()) continue
                    contact.birthday = value
                    val date = value.trim().replace("-", "")
                    when (date.length) {
                        8 -> {
                            contact.events += ContactEvent (
                                year = date.take(4).toIntOrNull(),
                                month = date.substring(4, 6).toIntOrNull(),
                                day = date.substring(6, 8).toIntOrNull(),
                                type = "birthday",
                            )
                        }
                        4 -> {
                            contact.events += ContactEvent (
                                month = date.take(2).toIntOrNull(),
                                day = date.substring(2, 4).toIntOrNull(),
                                type = "birthday",
                            )
                        }
                        else -> {
                            throw IllegalArgumentException("Invalid date format: $value")
                        }
                    }
                }
                col == "Notes" -> {
                    if (value.isEmpty()) continue
                    contact.note = value
                }
                col == "Photo" -> {
                    if (value.isEmpty()) continue
                    contact.photoBitmap = loadPicture(value)
                }
                col == "Labels" -> {
                    if (value.isEmpty()) continue
                    value.split(" ::: ").forEach {
                        val grpName = it.trim(' ', '*')
                        if (grpName == "starred") {
                            contact.isStarred = true
                        } else if (grpName != "myContacts") {
                            contact.groups += Group(
                                id = -1,
                                name = grpName,
                            )
                        }
                    }
                }
                col.startsWith("E-mail ") -> {
                    val mailIndex = col.substring(7, col.length - 8).toInt()
                    if (col.endsWith(" - Label")) {
                        emailLabels[mailIndex] = value
                    } else if (col.endsWith(" - Value")) {
                        if (value.isEmpty()) continue
                        emailAddresses[mailIndex] = value
                    }
                }
                col.startsWith("Phone ") -> {
                    val phoneIndex = col.substring(6, col.length - 8).toInt()
                    if (col.endsWith(" - Label")) {
                        phoneLabels[phoneIndex] = value
                    } else if (col.endsWith(" - Value")) {
                        if (value.isEmpty()) continue
                        phoneNumbers[phoneIndex] = value
                    }
                }
                col.startsWith("Relation ") -> {
                    val relationIndex = col.substring(9, col.length - 8).toInt()
                    if (col.endsWith(" - Label")) {
                        relationLabels[relationIndex] = value
                    } else if (col.endsWith(" - Value")) {
                        if (value.isEmpty()) continue
                        relationNames[relationIndex] = value
                    }
                }
                col.startsWith("Website ") -> {
                    val websiteIndex = col.substring(8, col.length - 8).toInt()
                    if (col.endsWith(" - Label")) {
                        websiteLabels[websiteIndex] = value
                    } else if (col.endsWith(" - Value")) {
                        if (value.isEmpty()) continue
                        websiteURLs[websiteIndex] = value
                    }
                }
                col.startsWith("Custom Field ") -> {
                    val customIndex = col.substring(13, col.length - 8).toInt()
                    if (col.endsWith(" - Label")) {
                        customLabels[customIndex] = value
                    } else if (col.endsWith(" - Value")) {
                        if (value.isEmpty()) continue
                        customValues[customIndex] = value
                    }
                }
                col.startsWith("Event ") -> {
                    val eventIndex = col.substring(6, col.length - 8).toInt()
                    if (col.endsWith(" - Label")) {
                        eventLabels[eventIndex] = value
                    } else if (col.endsWith(" - Value")) {
                        if (value.isEmpty()) continue
                        Log.d("Add EventDate", value)
                        eventDates[eventIndex] = value
                    }
                }
                col.startsWith("Address ") -> {
                    val addressIndex = col.substring(8).split("-")[0].trim().toInt()
                    if (col.endsWith(" - Label")) {
                        addressLabels[addressIndex] = value
                    } else if (col.endsWith(" - Street")) {
                        if (value.isEmpty()) continue
                        addressStreets[addressIndex] = value
                    } else if (col.endsWith(" - City")) {
                        if (value.isEmpty()) continue
                        addressCities[addressIndex] = value
                    } else if (col.endsWith(" - Region")) {
                        if (value.isEmpty()) continue
                        addressRegions[addressIndex] = value
                    } else if (col.endsWith(" - Postal Code")) {
                        if (value.isEmpty()) continue
                        addressPostcodes[addressIndex] = value
                    } else if (col.endsWith(" - Country")) {
                        if (value.isEmpty()) continue
                        addressCountries[addressIndex] = value
                    }
                }
                /*
                "Address 1 - Formatted"
                "Address 1 - PO Box"
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
                        address = add,
                        type = if (checkIfLabelIsType(label)) label.lowercase() else "custom",
                        label = if (checkIfLabelIsType(label)) "" else label,
                    )
                }
            }
        }
        for ((k, v) in phoneLabels) {
            if (phoneNumbers.containsKey(k) && !phoneNumbers[k].isNullOrBlank()) {
                val numbers = phoneNumbers[k]!!.split(" ::: ")
                val label = v.trim(' ', '*')
                for (num in numbers) {
                    contact.phoneNumbers += PhoneNumber(
                        number = num,
                        type = if (checkIfLabelIsType(label)) label.lowercase() else "custom",
                        label = if (checkIfLabelIsType(label)) "" else label,
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
                        name = name,
                        type = if (checkIfLabelIsType(label)) label.lowercase() else "custom",
                        label = if (checkIfLabelIsType(label)) "" else label,
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
                        url = url,
                        type = if (checkIfLabelIsType(label)) label.lowercase() else "custom",
                        label = if (checkIfLabelIsType(label)) "" else label,
                    )
                }
            }
        }
        for ((k, v) in customLabels) {
            if (customValues.containsKey(k) && !customValues[k].isNullOrBlank()) {
                val values = customValues[k]!!.split(" ::: ")
                val label = v.trim(' ', '*')
                if (values.size == 1 && values[0] == "Display Name") {
                    contact.displayName = label
                    continue
                }
                for (value in values) {
                    contact.customFields += Pair(label, value)
                }
            }
        }
        Log.d("EventDates", eventDates.toString())
        for ((k, v) in eventLabels) {
            if (eventDates.containsKey(k) && !eventDates[k].isNullOrBlank()) {
                val dates = eventDates[k]!!.split(" ::: ").map { it.trim().replace("-", "") }
                val label = v.trim(' ', '*')
                for (date in dates) {
                    when (date.length) {
                        8 -> {
                            contact.events += ContactEvent (
                                year = date.take(4).toIntOrNull(),
                                month = date.substring(4, 6).toIntOrNull(),
                                day = date.substring(6, 8).toIntOrNull(),
                                type = if (checkIfLabelIsType(label)) label.lowercase() else "custom",
                                label = if (checkIfLabelIsType(label)) "" else label,
                            )
                        }
                        4 -> {
                            contact.events += ContactEvent (
                                month = date.take(2).toIntOrNull(),
                                day = date.substring(2, 4).toIntOrNull(),
                                type = if (checkIfLabelIsType(label)) label.lowercase() else "custom",
                                label = if (checkIfLabelIsType(label)) "" else label,
                            )
                        }
                        else -> {
                            throw IllegalArgumentException("Invalid date format: $date")
                        }
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
                contact.addresses += PostalAddress(
                    street = streets?.get(l),
                    city = cities?.get(l),
                    region = regions?.get(l),
                    postcode = postcodes?.get(l),
                    country = countries?.get(l),
                    type = if (checkIfLabelIsType(label)) label.lowercase() else "custom",
                    label = if (checkIfLabelIsType(label)) "" else label,
                )
            }
        }
        contacts.add(contact)
    }
    return contacts
}