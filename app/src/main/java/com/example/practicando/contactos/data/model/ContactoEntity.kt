package com.example.practicando.contactos.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContactoEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var nombre: String,
    var telefono: Int
)