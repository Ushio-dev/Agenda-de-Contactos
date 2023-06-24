package com.example.practicando.contactos.screens

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicando.contactos.data.model.Contacto
import com.example.practicando.contactos.data.model.ContactoEntity
import com.example.practicando.contactos.domain.AddContactUseCase
import com.example.practicando.contactos.domain.DeleteContactUseCase
import com.example.practicando.contactos.domain.GetContactsUseCase
import com.example.practicando.contactos.screens.ContactoUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactosViewModel @Inject constructor(
    private val addContactUseCase: AddContactUseCase,
    getContactsUseCase: GetContactsUseCase,
    private val deleteContactsUseCase: DeleteContactUseCase
) : ViewModel() {

    val uiState: StateFlow<ContactoUiState> = getContactsUseCase().map(::Success )
        .catch { ContactoUiState.Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ContactoUiState.Loading)

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

    //private val _contactos = mutableStateListOf<Contacto>()
    //val contactos: List<Contacto> = _contactos

    fun onDialogClose() {
        _showDialog.value = false
    }

    fun onContactCreated(contacto: Contacto) {
        _showDialog.value = false

        viewModelScope.launch {
            val newContact = ContactoEntity(nombre = contacto.nombre, telefono = contacto.numero, id = 0)
            addContactUseCase(newContact)
        }
    }

    fun onShowDialogClick() {
        _showDialog.value = true
    }

    fun onContactRemove(contacto: ContactoEntity) {
        viewModelScope.launch {
            deleteContactsUseCase(contacto)
        }
    }
}