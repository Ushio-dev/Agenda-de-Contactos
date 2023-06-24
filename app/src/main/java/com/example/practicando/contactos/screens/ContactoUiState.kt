package com.example.practicando.contactos.screens

import com.example.practicando.contactos.data.model.ContactoEntity

sealed interface ContactoUiState {
    object Loading: ContactoUiState
    data class Error(val throwable: Throwable): ContactoUiState
    data class Success(val contactos: List<ContactoEntity>): ContactoUiState
}