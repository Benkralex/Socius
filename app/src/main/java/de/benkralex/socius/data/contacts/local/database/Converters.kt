package de.benkralex.socius.data.contacts.local.database

import androidx.room.TypeConverter
import de.benkralex.socius.data.ContactEvent
import de.benkralex.socius.data.Email
import de.benkralex.socius.data.Group
import de.benkralex.socius.data.PhoneNumber
import de.benkralex.socius.data.PostalAddress
import de.benkralex.socius.data.Relation
import de.benkralex.socius.data.Website
import kotlinx.serialization.json.Json


class Converters {
    @TypeConverter
    fun toPhoneNumberList(value: String): List<PhoneNumber> {
        return Json.decodeFromString<List<PhoneNumber>>(value)
    }

    @TypeConverter
    fun fromPhoneNumberList(list: List<PhoneNumber>): String {
        return Json.encodeToString(list)
    }


    @TypeConverter
    fun toEmailList(value: String): List<Email> {
        return Json.decodeFromString<List<Email>>(value)
    }

    @TypeConverter
    fun fromEmailList(list: List<Email>): String {
        return Json.encodeToString(list)
    }


    @TypeConverter
    fun toPostalAddressList(value: String): List<PostalAddress> {
        return Json.decodeFromString<List<PostalAddress>>(value)
    }

    @TypeConverter
    fun fromPostalAddressList(list: List<PostalAddress>): String {
        return Json.encodeToString(list)
    }


    @TypeConverter
    fun toWebsiteList(value: String): List<Website> {
        return Json.decodeFromString<List<Website>>(value)
    }

    @TypeConverter
    fun fromWebsiteList(list: List<Website>): String {
        return Json.encodeToString(list)
    }


    @TypeConverter
    fun toRelationList(value: String): List<Relation> {
        return Json.decodeFromString<List<Relation>>(value)
    }

    @TypeConverter
    fun fromRelationList(list: List<Relation>): String {
        return Json.encodeToString(list)
    }


    @TypeConverter
    fun toContactEventList(value: String): List<ContactEvent> {
        return Json.decodeFromString<List<ContactEvent>>(value)
    }

    @TypeConverter
    fun fromContactEventList(list: List<ContactEvent>): String {
        return Json.encodeToString(list)
    }


    @TypeConverter
    fun toGroupList(value: String): List<Group> {
        return Json.decodeFromString<List<Group>>(value)
    }

    @TypeConverter
    fun fromGroupList(list: List<Group>): String {
        return Json.encodeToString(list)
    }


    @TypeConverter
    fun toStringStringMap(value: String): Map<String, String> {
        return Json.decodeFromString<Map<String, String>>(value)
    }

    @TypeConverter
    fun fromStringStringMap(map: Map<String, String>): String {
        return Json.encodeToString(map)
    }
}