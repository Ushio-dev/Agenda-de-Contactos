package com.example.practicando.contactos.domain

import com.example.practicando.contactos.data.ContactoRepository
import com.example.practicando.contactos.data.model.ContactoEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetContactsUseCase @Inject constructor(
    private val contacoRepository: ContactoRepository
) {
    operator fun invoke(): Flow<List<ContactoEntity>> {
        return contacoRepository.contactos
    }
}