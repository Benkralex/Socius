package de.benkralex.socius.data.contacts.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [LocalContactsEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class LocalContactsDatabase : RoomDatabase() {
    abstract fun localContactsDao(): LocalContactsDao

    companion object {
        @Volatile private var INSTANCE: LocalContactsDatabase? = null

        fun getDatabase(context: Context): LocalContactsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalContactsDatabase::class.java,
                    "local_contacts_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}