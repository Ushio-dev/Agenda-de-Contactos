package com.example.practicando.contactos.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.practicando.contactos.data.model.ContactoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactoDao {

    @Query("SELECT * FROM ContactoEntity")
    fun getContacts(): Flow<List<ContactoEntity>>

    @Insert
    suspend fun addContact(contacto: ContactoEntity)

    @Delete
    suspend fun deleteContact(contacto: ContactoEntity)
}