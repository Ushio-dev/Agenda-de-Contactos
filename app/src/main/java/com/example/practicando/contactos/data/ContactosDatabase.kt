package com.example.practicando.contactos.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.practicando.contactos.data.model.ContactoEntity

@Database(entities = [ContactoEntity::class], version = 1, exportSchema = false)
abstract class ContactosDatabase : RoomDatabase() {
    // DAO
    abstract fun contactoDao(): ContactoDao
}