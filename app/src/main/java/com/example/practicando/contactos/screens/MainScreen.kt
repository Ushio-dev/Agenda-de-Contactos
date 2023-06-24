package com.example.practicando.contactos.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.practicando.R
import com.example.practicando.contactos.data.model.Contacto
import com.example.practicando.contactos.data.model.ContactoEntity

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(contactosViewModel: ContactosViewModel) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val showDialog: Boolean by contactosViewModel.showDialog.observeAsState(false)

    val uiState by produceState<ContactoUiState>(
        initialValue = ContactoUiState.Loading,
        key1 = lifecycle,
        key2 = contactosViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            contactosViewModel.uiState.collect { value = it }
        }
    }

    when (uiState) {
        is ContactoUiState.Error -> {}
        ContactoUiState.Loading -> {
            CircularProgressIndicator()
        }

        is ContactoUiState.Success -> {
            Scaffold(
                topBar = { MyAppBar() },
                floatingActionButton = { MyFab(contactosViewModel) }
            ) {
                MyAlertForm(
                    showDialow = showDialog,
                    onDismiss = { contactosViewModel.onDialogClose() },
                    onConctactAdded = { contactosViewModel.onContactCreated(it) })
                Listas(it, (uiState as ContactoUiState.Success).contactos, contactosViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyAppBar() {
    TopAppBar(title = { Text(text = "Practica") }, actions = {
        IconButton(onClick = {}) {
            Icon(imageVector = Icons.Filled.Search, contentDescription = "search")
        }
    })
}

@Composable
private fun Listas(
    paddingValues: PaddingValues,
    contactos: List<ContactoEntity>,
    contactosViewModel: ContactosViewModel
) {
    LazyColumn(contentPadding = paddingValues) {
        items(
            contactos,
            key = { it.id }) { // por el momento sin el key, pero es buena idea darle una identtidad unica a los elementos
            ContactCard(contacto = it, contactosViewModel = contactosViewModel)
        }
    }
}

@Composable
private fun ContactCard(contacto: ContactoEntity, contactosViewModel: ContactosViewModel) {
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    contactosViewModel.onContactRemove(contacto)
                })
            }
    ) {
        Column() {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(color = Color.LightGray, shape = CircleShape)
                        .size(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.mugi),
                        contentDescription = null,
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(40.dp)
                    )
                }
                Spacer(modifier = Modifier.width(32.dp))
                Column(Modifier.fillMaxWidth()) {
                    Text(
                        text = contacto.nombre,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 15.sp
                    )
                    Text(
                        text = contacto.telefono.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun MyFab(contactosViewModel: ContactosViewModel) {
    FloatingActionButton(onClick = {
        contactosViewModel.onShowDialogClick()
    }) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = null)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyAlertForm(
    showDialow: Boolean,
    onDismiss: () -> Unit,
    onConctactAdded: (Contacto) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }

    if (showDialow) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            title = { Text(text = "Nuevo Contacto") },
            text = {
                Column() {
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text(text = "Nombre") })
                    OutlinedTextField(
                        value = numero,
                        onValueChange = { numero = it },
                        label = {
                            Text(
                                text = "Telefono"
                            )
                        }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    onConctactAdded(Contacto(nombre, numero.toInt(), "3132"))
                    nombre = ""
                    numero = ""
                }) {
                    Text(text = "Guardar")
                }
            }, dismissButton = {
                Button(onClick = { onDismiss() }) {
                    Text(text = "Cancelar")
                }
            })
    }
}