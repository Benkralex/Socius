package de.benkralex.socius.data.contacts.local.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import de.benkralex.socius.data.model.ContactOrigin
import de.benkralex.socius.data.model.Name
import de.benkralex.socius.data.model.Job
import de.benkralex.socius.data.model.ProfilePicture
import de.benkralex.socius.data.model.Phone
import de.benkralex.socius.data.model.Email
import de.benkralex.socius.data.model.Address
import de.benkralex.socius.data.model.Event
import de.benkralex.socius.data.model.Relation
import de.benkralex.socius.data.model.Website
import de.benkralex.socius.data.model.Type
import de.benkralex.socius.data.model.old.PhoneNumber
import de.benkralex.socius.data.model.old.PostalAddress
import de.benkralex.socius.data.model.old.ContactEvent
import de.benkralex.socius.data.model.old.Group
import kotlinx.serialization.json.Json

@Database(
    entities = [LocalContactsEntity::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class LocalContactsDatabase : RoomDatabase() {
    abstract fun localContactsDao(): LocalContactsDao

    companion object {
        @Volatile private var INSTANCE: LocalContactsDatabase? = null

        private val json = Json { ignoreUnknownKeys = true }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE local_contacts ADD COLUMN photoBitmap BLOB"
                )
                db.execSQL(
                    "ALTER TABLE local_contacts ADD COLUMN thumbnailBitmap BLOB"
                )
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `LocalContacts_new` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `origin` TEXT NOT NULL,
                        `name` TEXT NOT NULL,
                        `job` TEXT NOT NULL,
                        `profilePicture` TEXT NOT NULL,
                        `phoneNumbers` TEXT NOT NULL,
                        `emails` TEXT NOT NULL,
                        `addresses` TEXT NOT NULL,
                        `websites` TEXT NOT NULL,
                        `relations` TEXT NOT NULL,
                        `events` TEXT NOT NULL,
                        `groups` TEXT NOT NULL,
                        `note` TEXT,
                        `isStarred` INTEGER NOT NULL,
                        `customFields` TEXT NOT NULL
                    )
                    """.trimIndent()
                )

                db.query("SELECT * FROM `local_contacts`").use { cursor ->
                    while (cursor.moveToNext()) {
                        val id = cursor.getIntByName("id")
                        val name = Name(
                            prefix = cursor.getStringByName("prefix"),
                            firstname = cursor.getStringByName("givenName"),
                            secondName = cursor.getStringByName("middleName"),
                            lastname = cursor.getStringByName("familyName"),
                            suffix = cursor.getStringByName("suffix"),
                            nickname = cursor.getStringByName("nickname"),
                            phoneticFirstname = cursor.getStringByName("phoneticGivenName"),
                            phoneticSecondName = cursor.getStringByName("phoneticMiddleName"),
                            phoneticLastname = cursor.getStringByName("phoneticFamilyName"),
                        )
                        val job = Job(
                            organization = cursor.getStringByName("organization"),
                            department = cursor.getStringByName("department"),
                            jobTitle = cursor.getStringByName("jobTitle")
                        )

                        val photoBitmap = decodeBitmap(cursor.getBlobByName("photoBitmap"))
                        val thumbnailBitmap = decodeBitmap(cursor.getBlobByName("thumbnailBitmap"))
                        val profilePicture = ProfilePicture(bitmap = photoBitmap ?: thumbnailBitmap)

                        val phoneNumbers = decodeOrDefault<List<PhoneNumber>>(cursor.getStringByName("phoneNumbers"), emptyList())
                            .map { old -> Phone(value = old.number, type = mapPhoneType(old.type)) }
                        val emails = decodeOrDefault<List<de.benkralex.socius.data.model.old.Email>>(cursor.getStringByName("emails"), emptyList())
                            .map { old -> Email(value = old.address, type = mapEmailType(old.type)) }
                        val addresses = decodeOrDefault<List<PostalAddress>>(cursor.getStringByName("addresses"), emptyList())
                            .map { old ->
                                Address(
                                    street = old.street,
                                    city = old.city,
                                    postcode = old.postcode?.toIntOrNull(),
                                    region = old.region,
                                    country = old.country,
                                    type = mapAddressType(old.type)
                                )
                            }
                        val websites = decodeOrDefault<List<de.benkralex.socius.data.model.old.Website>>(cursor.getStringByName("websites"), emptyList())
                            .map { old -> Website(value = old.url, type = mapWebsiteType(old.type)) }
                        val relations = decodeOrDefault<List<de.benkralex.socius.data.model.old.Relation>>(cursor.getStringByName("relations"), emptyList())
                            .map { old -> Relation(value = old.name, type = mapRelationType(old.type)) }
                        val events = decodeOrDefault<List<ContactEvent>>(cursor.getStringByName("events"), emptyList())
                            .filter { it.day != null && it.month != null }
                            .map { old -> Event(day = old.day!!, month = old.month!!, year = old.year, type = mapEventType(old.type)) }
                        val groups = decodeOrDefault<List<Group>>(cursor.getStringByName("groups"), emptyList())
                            .mapNotNull { it.name?.takeIf { value -> value.isNotBlank() } }
                        val customFields = decodeOrDefault<Map<String, String>>(cursor.getStringByName("customFields"), emptyMap())

                        val values = ContentValues().apply {
                            put("id", id)
                            put("origin", ContactOrigin.LOCAL.name)
                            put("name", json.encodeToString(name))
                            put("job", json.encodeToString(job))
                            put("profilePicture", json.encodeToString(profilePicture))
                            put("phoneNumbers", json.encodeToString(phoneNumbers))
                            put("emails", json.encodeToString(emails))
                            put("addresses", json.encodeToString(addresses))
                            put("websites", json.encodeToString(websites))
                            put("relations", json.encodeToString(relations))
                            put("events", json.encodeToString(events))
                            put("groups", json.encodeToString(groups))
                            put("note", cursor.getStringByName("note"))
                            put("isStarred", cursor.getBooleanAsIntByName("isStarred"))
                            put("customFields", json.encodeToString(customFields))
                        }

                        db.insert("LocalContacts_new", SQLiteDatabase.CONFLICT_REPLACE, values)
                    }
                }

                db.execSQL("DROP TABLE `local_contacts`")
                db.execSQL("ALTER TABLE `LocalContacts_new` RENAME TO `LocalContacts`")
            }
        }

        fun getDatabase(context: Context): LocalContactsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalContactsDatabase::class.java,
                    "local_contacts_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private inline fun <reified T> decodeOrDefault(raw: String?, default: T): T {
            if (raw.isNullOrBlank()) return default
            return runCatching { json.decodeFromString<T>(raw) }.getOrDefault(default)
        }

        private fun decodeBitmap(bytes: ByteArray?): Bitmap? {
            if (bytes == null) return null
            return runCatching { BitmapFactory.decodeByteArray(bytes, 0, bytes.size) }.getOrNull()
        }

        private fun Cursor.getStringByName(columnName: String): String? {
            val index = getColumnIndex(columnName)
            if (index < 0 || isNull(index)) return null
            return getString(index)
        }

        private fun Cursor.getBlobByName(columnName: String): ByteArray? {
            val index = getColumnIndex(columnName)
            if (index < 0 || isNull(index)) return null
            return getBlob(index)
        }

        private fun Cursor.getIntByName(columnName: String): Int {
            val index = getColumnIndex(columnName)
            return if (index < 0 || isNull(index)) 0 else getInt(index)
        }

        private fun Cursor.getBooleanAsIntByName(columnName: String): Int {
            val index = getColumnIndex(columnName)
            return if (index < 0 || isNull(index)) 0 else if (getInt(index) != 0) 1 else 0
        }

        private fun mapPhoneType(value: String?): Type.Phone {
            return when (value?.trim()?.lowercase()) {
                "home" -> Type.Phone.HOME
                "work" -> Type.Phone.WORK
                "mobile", "cell", "cellphone" -> Type.Phone.MOBILE_HOME
                "mobile_work", "work_mobile", "work cell", "work mobile" -> Type.Phone.MOBILE_WORK
                "fax", "fax_home", "home fax" -> Type.Phone.FAX_HOME
                "fax_work", "work fax" -> Type.Phone.FAX_WORK
                "pager", "pager_home", "home pager" -> Type.Phone.PAGER_HOME
                "pager_work", "work pager" -> Type.Phone.PAGER_WORK
                else -> {
                    val type = Type.Phone.CUSTOM
                    type.label = value
                    type
                }
            }
        }

        private fun mapEmailType(value: String?): Type.Email {
            return when (value?.trim()?.lowercase()) {
                "home" -> Type.Email.HOME
                "work" -> Type.Email.WORK
                else -> {
                    val type = Type.Email.CUSTOM
                    type.label = value
                    type
                }
            }
        }

        private fun mapAddressType(value: String?): Type.Address {
            return when (value?.trim()?.lowercase()) {
                "home" -> Type.Address.HOME
                "work" -> Type.Address.WORK
                else -> {
                    val type = Type.Address.CUSTOM
                    type.label = value
                    type
                }
            }
        }

        private fun mapWebsiteType(value: String?): Type.Website {
            return when (value?.trim()?.lowercase()) {
                "homepage", "home" -> Type.Website.HOMEPAGE
                "blog" -> Type.Website.BLOG
                else -> {
                    val type = Type.Website.CUSTOM
                    type.label = value
                    type
                }
            }
        }

        private fun mapEventType(value: String?): Type.Event {
            return when (value?.trim()?.lowercase()) {
                "birthday" -> Type.Event.BIRTHDAY
                "anniversary" -> Type.Event.ANNIVERSARY
                else -> {
                    val type = Type.Event.CUSTOM
                    type.label = value
                    type
                }
            }
        }

        private fun mapRelationType(value: String?): Type.Relation {
            return when (value?.trim()?.lowercase()) {
                "spouse" -> Type.Relation.SPOUSE
                "child" -> Type.Relation.CHILD
                "mother" -> Type.Relation.MOTHER
                "father" -> Type.Relation.FATHER
                "parent" -> Type.Relation.PARENT
                "brother" -> Type.Relation.BROTHER
                "sister" -> Type.Relation.SISTER
                "friend" -> Type.Relation.FRIEND
                "relative" -> Type.Relation.RELATIVE
                "domestic_partner", "domestic partner" -> Type.Relation.DOMESTIC_PARTNER
                "manager" -> Type.Relation.MANAGER
                "assistant" -> Type.Relation.ASSISTANT
                "referred_by", "referred by" -> Type.Relation.REFERRED_BY
                "partner" -> Type.Relation.PARTNER
                else -> {
                    val type = Type.Relation.CUSTOM
                    type.label = value
                    type
                }
            }
        }
    }
}
