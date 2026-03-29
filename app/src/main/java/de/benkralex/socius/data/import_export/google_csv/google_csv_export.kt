package de.benkralex.socius.data.import_export.google_csv

import de.benkralex.socius.data.model.Contact
import de.benkralex.socius.data.model.Event
import de.benkralex.socius.data.model.Type

fun prepareStringForCsvValue(s: String): String {
    if (s.contains("\n") || s.contains(",")) {
        return "\"" + s
            .replace("\"", "\\\"") +
                "\""
    }
    return s
}

fun formatDate(year: Int?, month: Int, day: Int): String {
    return "${year ?: "-"}-${if (month < 10) "0$month" else month}-${if (day < 10) "0$day" else day}"
}

fun getFilteredEvents(c: Contact): List<Event> {
    if (!c.events.map { it.type }.contains(Type.Event.BIRTHDAY)) return c.events

    val firstBirthday = c.events.first { it.type == Type.Event.BIRTHDAY }
    return c.events - firstBirthday
}

fun getBirthdayString(c: Contact): String? {
    if (!c.events.map { it.type }.contains(Type.Event.BIRTHDAY)) return null

    val firstBirthday = c.events.first { it.type == Type.Event.BIRTHDAY }
    return formatDate(firstBirthday.year, firstBirthday.month, firstBirthday.day)
}

fun contactsToGoogleCsv(contacts: List<Contact>): List<String> {
    val header: MutableList<String> = mutableListOf(
        "First Name",
        "Middle Name",
        "Last Name",
        "Phonetic First Name",
        "Phonetic Middle Name",
        "Phonetic Last Name",
        "Name Prefix",
        "Name Suffix",
        "Nickname",
        "File As",
        "Organization Name",
        "Organization Title",
        "Organization Department",
        "Birthday",
        "Notes",
        "Photo",
        "Labels",
    )
    val emailCount = contacts.maxOfOrNull { it.emails.size } ?: 0
    val phoneCount = contacts.maxOfOrNull { it.phoneNumbers.size } ?: 0
    val addressCount = contacts.maxOfOrNull { it.addresses.size } ?: 0
    val relationCount = contacts.maxOfOrNull { it.relations.size } ?: 0
    val websiteCount = contacts.maxOfOrNull { it.websites.size } ?: 0
    val eventsCount = contacts.maxOfOrNull { getFilteredEvents(it).size } ?: 0
    val customFieldCount = contacts.maxOfOrNull { it.customFields.size } ?: 0

    for (i in 0..<emailCount) {
        header.add("E-mail ${i+1} - Label")
        header.add("E-mail ${i+1} - Value")
    }
    for (i in 0..<phoneCount) {
        header.add("Phone ${i+1} - Label")
        header.add("Phone ${i+1} - Value")
    }
    for (i in 0..<addressCount) {
        header.add("Address ${i+1} - Label")
        header.add("Address ${i+1} - Formatted")
        header.add("Address ${i+1} - Street")
        header.add("Address ${i+1} - City")
        header.add("Address ${i+1} - PO Box")
        header.add("Address ${i+1} - Region")
        header.add("Address ${i+1} - Postal Code")
        header.add("Address ${i+1} - Country")
        header.add("Address ${i+1} - Extended Address")
    }
    for (i in 0..<relationCount) {
        header.add("Relation ${i+1} - Label")
        header.add("Relation ${i+1} - Value")
    }
    for (i in 0..<websiteCount) {
        header.add("Website ${i+1} - Label")
        header.add("Website ${i+1} - Value")
    }
    for (i in 0..<eventsCount) {
        header.add("Event ${i+1} - Label")
        header.add("Event ${i+1} - Value")
    }
    for (i in 0..<customFieldCount) {
        header.add("Custom Field ${i+1} - Label")
        header.add("Custom Field ${i+1} - Value")
    }


    val bodyLines: MutableList<MutableList<String>> = mutableListOf()
    for (c in contacts) {
        bodyLines.add(mutableListOf(
            prepareStringForCsvValue(c.name.firstname ?: ""),
            prepareStringForCsvValue(c.name.secondName ?: ""),
            prepareStringForCsvValue(c.name.lastname ?: ""),
            prepareStringForCsvValue(c.name.phoneticFirstname ?: ""),
            prepareStringForCsvValue(c.name.phoneticSecondName ?: ""),
            prepareStringForCsvValue(c.name.phoneticLastname ?: ""),
            prepareStringForCsvValue(c.name.prefix ?: ""),
            prepareStringForCsvValue(c.name.suffix ?: ""),
            prepareStringForCsvValue(c.name.nickname ?: ""),
            prepareStringForCsvValue(c.name.displayName ?: ""),
            prepareStringForCsvValue(c.job.organization ?: ""),
            prepareStringForCsvValue(c.job.jobTitle ?: ""),
            prepareStringForCsvValue(c.job.department ?: ""),
            prepareStringForCsvValue(getBirthdayString(c) ?: ""),
            prepareStringForCsvValue(c.note ?: ""),
            "",
            prepareStringForCsvValue((c.groups + listOfNotNull(if (c.isStarred) "starred" else null)).joinToString(" ::: ")),
        ))
        for (i in 0..<emailCount) {
            if (i >= c.emails.size) {
                bodyLines.last().addAll(listOf("", ""))
                continue
            }
            bodyLines.last().add(
                prepareStringForCsvValue(Type.convertToString(c.emails[i].type))
            )
            bodyLines.last().add(
                prepareStringForCsvValue(c.emails[i].value)
            )
        }
        for (i in 0..<phoneCount) {
            if (i >= c.phoneNumbers.size) {
                bodyLines.last().addAll(listOf("", ""))
                continue
            }
            bodyLines.last().add(
                prepareStringForCsvValue(Type.convertToString(c.phoneNumbers[i].type))
            )
            bodyLines.last().add(
                prepareStringForCsvValue(c.phoneNumbers[i].value)
            )
        }
        for (i in 0..<addressCount) {
            if (i >= c.addresses.size) {
                bodyLines.last().addAll(listOf("", "", "", "", "", "", "", "", ""))
                continue
            }
            val street = listOfNotNull(c.addresses[i].street, c.addresses[i].streetNumber).joinToString(" ")
            val city = c.addresses[i].city ?: ""
            val region = c.addresses[i].region ?: ""
            val postcode = c.addresses[i].postcode
            val country = c.addresses[i].country ?: ""

            //Label
            bodyLines.last().add(
                prepareStringForCsvValue(Type.convertToString(c.addresses[i].type))
            )

            //Formatted
            val addressParts = mutableListOf<String>()
            if (street.isNotBlank()) addressParts.add(street)
            if (postcode != null || !city.isNotBlank()) {
                addressParts.add(listOfNotNull(postcode, city).joinToString(" "))
            }
            if (region.isNotBlank()) addressParts.add(region)
            if (country.isNotBlank()) addressParts.add(country)
            bodyLines.last().add(prepareStringForCsvValue(addressParts.joinToString("\n")))

            //Street
            bodyLines.last().add(prepareStringForCsvValue(street))
            //City
            bodyLines.last().add(prepareStringForCsvValue(city))
            //PO Box
            bodyLines.last().add("")
            //Region
            bodyLines.last().add(prepareStringForCsvValue(region))
            //Postal Code
            bodyLines.last().add(prepareStringForCsvValue(postcode?.toString() ?: ""))
            //Country
            bodyLines.last().add(prepareStringForCsvValue(country))
            //Extended Address
            bodyLines.last().add("")
        }
        for (i in 0..<relationCount) {
            if (i >= c.relations.size) {
                bodyLines.last().addAll(listOf("", ""))
                continue
            }
            bodyLines.last().add(
                prepareStringForCsvValue(Type.convertToString(c.relations[i].type))
            )
            bodyLines.last().add(
                prepareStringForCsvValue(c.relations[i].value)
            )
        }
        for (i in 0..<websiteCount) {
            if (i >= c.websites.size) {
                bodyLines.last().addAll(listOf("", ""))
                continue
            }
            bodyLines.last().add(
                prepareStringForCsvValue(Type.convertToString(c.websites[i].type))
            )
            bodyLines.last().add(
                prepareStringForCsvValue(c.websites[i].value)
            )
        }
        val events: List<Event> = getFilteredEvents(c)
        for (i in 0..<eventsCount) {
            if (i >= events.size) {
                bodyLines.last().addAll(listOf("", ""))
                continue
            }
            bodyLines.last().add(
                prepareStringForCsvValue(Type.convertToString(c.events[i].type))
            )
            bodyLines.last().add(
                prepareStringForCsvValue(formatDate(events[i].year, events[i].month, events[i].day))
            )
        }
        val customFields: List<Pair<String, String>> = c.customFields.toList()
        for (i in 0..<customFieldCount) {
            if (i >= customFields.size) {
                bodyLines.last().addAll(listOf("", ""))
                continue
            }
            bodyLines.last().add(prepareStringForCsvValue(customFields[i].first))
            bodyLines.last().add(prepareStringForCsvValue(customFields[i].second))
        }
    }
    return listOf(header.joinToString(",")).plus(bodyLines.map { it.joinToString(",") })
}