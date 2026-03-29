package de.benkralex.socius.data.contacts.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [LocalContactsEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class LocalContactsDatabase : RoomDatabase() {
    abstract fun localContactsDao(): LocalContactsDao

    companion object {
        @Volatile private var INSTANCE: LocalContactsDatabase? = null

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

        fun getDatabase(context: Context): LocalContactsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalContactsDatabase::class.java,
                    "local_contacts_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}