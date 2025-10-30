package de.benkralex.socius.data.contacts.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocalContactsDao {
    @Insert
    suspend fun insert(entity: LocalContactsEntity)

    @Query("SELECT * FROM local_contacts")
    suspend fun getAll(): List<LocalContactsEntity>

    @Query("SELECT * FROM local_contacts WHERE id = :id")
    suspend fun getById(id: Int): LocalContactsEntity?

    @Query("DELETE FROM local_contacts WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM local_contacts")
    suspend fun deleteAll()

    @Query("UPDATE local_contacts SET isStarred = :isStarred WHERE id = :id")
    suspend fun updateStarredStatus(id: Int, isStarred: Boolean)
}