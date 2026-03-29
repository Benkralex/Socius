package de.benkralex.socius.data.contacts.local.database

import androidx.room.TypeConverter
import de.benkralex.socius.data.model.Address
import de.benkralex.socius.data.model.Email
import de.benkralex.socius.data.model.Event
import de.benkralex.socius.data.model.Job
import de.benkralex.socius.data.model.Name
import de.benkralex.socius.data.model.Phone
import de.benkralex.socius.data.model.ProfilePicture
import de.benkralex.socius.data.model.Relation
import de.benkralex.socius.data.model.Website
import kotlinx.serialization.json.Json

class LocalContactsTypeConverter {
    private val json = Json { prettyPrint = true }

    //Name
    @TypeConverter
    fun fromName(value: Name): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toName(string: String): Name? {
        return try {
            json.decodeFromString<Name>(string)
        } catch (e: Exception) {
            null
        }
    }

    //Job
    @TypeConverter
    fun fromJob(value: Job): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toJob(string: String): Job? {
        return try {
            json.decodeFromString<Job>(string)
        } catch (e: Exception) {
            null
        }
    }

    //ProfilePicture
    @TypeConverter
    fun fromProfilePicture(value: ProfilePicture): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toProfilePicture(string: String): ProfilePicture? {
        return try {
            json.decodeFromString<ProfilePicture>(string)
        } catch (e: Exception) {
            null
        }
    }

    //Phone
    @TypeConverter
    fun fromPhone(value: ProfilePicture): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toPhone(string: String): Phone? {
        return try {
            json.decodeFromString<Phone>(string)
        } catch (e: Exception) {
            null
        }
    }

    //Email
    @TypeConverter
    fun fromEmail(value: ProfilePicture): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toEmail(string: String): Email? {
        return try {
            json.decodeFromString<Email>(string)
        } catch (e: Exception) {
            null
        }
    }

    //Address
    @TypeConverter
    fun fromAddress(value: ProfilePicture): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toAddress(string: String): Address? {
        return try {
            json.decodeFromString<Address>(string)
        } catch (e: Exception) {
            null
        }
    }

    //Website
    @TypeConverter
    fun fromWebsite(value: ProfilePicture): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toWebsite(string: String): Website? {
        return try {
            json.decodeFromString<Website>(string)
        } catch (e: Exception) {
            null
        }
    }

    //Relation
    @TypeConverter
    fun fromRelation(value: ProfilePicture): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toRelation(string: String): Relation? {
        return try {
            json.decodeFromString<Relation>(string)
        } catch (e: Exception) {
            null
        }
    }

    //Event
    @TypeConverter
    fun fromEvent(value: ProfilePicture): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toEvent(string: String): Event? {
        return try {
            json.decodeFromString<Event>(string)
        } catch (e: Exception) {
            null
        }
    }

}