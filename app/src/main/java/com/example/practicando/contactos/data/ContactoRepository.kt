package com.example.practicando.contactos.data

import com.example.practicando.contactos.data.model.ContactoEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactoRepository @Inject constructor(private val contactoDao: ContactoDao) {

    val contactos: Flow<List<ContactoEntity>> = contactoDao.getContacts()

    suspend fun add(contactoEntity: ContactoEntity) {
        contactoDao.addContact(contactoEntity)
    }

    suspend fun delete(contactoEntity: ContactoEntity) {
        contactoDao.deleteContact(contactoEntity)
    }
}