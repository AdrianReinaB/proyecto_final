package com.example.proyect_final.ui.screens.admin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.proyect_final.R
import com.example.proyect_final.data.Usuario
import com.example.proyect_final.data.UsuarioRegister
import com.example.proyect_final.data.UsuarioUpdate
import com.example.proyect_final.network.RetrofitClient
import com.example.proyect_final.utility.isValidDni
import com.example.proyect_final.utility.isValidPhone
import com.example.proyect_final.viewModel.UserViewModel

@Composable
fun UserListScreen(user: UserViewModel) {
    var showSearch by remember { mutableStateOf(false) }
    var search by remember { mutableStateOf("") }

    val users by user.users.collectAsState()

    val us = user.currentUser.value



    LaunchedEffect(Unit) {
        user.loadUsers()
    }

    val filteredRents = users?.filter {
        it.nombre.contains(search, ignoreCase = true)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { showSearch = !showSearch }) {
                Icon(
                    imageVector = Icons.Default.Search, contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            if (showSearch) {
                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    label = { Text(stringResource(R.string.Buscar_usuario)) }
                )
            }
        }
        if (filteredRents != null) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(filteredRents) { users ->
                    if (us != null) {
                        UserCardList(
                            usu = users,
                            us_id = us.id_usuario,
                            onUpdate = { id, us -> user.updateUser(id, us) },
                            onActivate = { id -> user.toggleUserActive(id) })
                    }
                }
            }
        }
    }
}

@Composable
fun UserCardList(
    usu: Usuario,
    us_id: Int,
    onUpdate: (Int, UsuarioUpdate) -> Unit,
    onActivate: (Int) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    var newNombre by remember { mutableStateOf(usu.nombre ?: "") }
    var newApellido by remember { mutableStateOf(usu.apellido ?: "") }
    var newPassword by remember { mutableStateOf("") }
    var newDni by remember { mutableStateOf(usu.dni ?: "") }
    var newEmail by remember { mutableStateOf(usu.email ?: "") }
    var newRol by remember { mutableStateOf(usu.rol ?: "") }
    var newTelefono by remember { mutableStateOf(usu.telefono ?: "") }
    var newCredito by remember { mutableStateOf(usu.credito ?: "") }

    var isValidDNI by remember { mutableStateOf(false) }
    var isValidMail by remember { mutableStateOf(false) }
    var isValidTelefono by remember { mutableStateOf(false) }

    val isCurrentUser = usu.id_usuario == us_id

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = usu.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                    contentDescription = "",
                    modifier = Modifier.clickable { expanded = !expanded },
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            AnimatedVisibility(expanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {

                    EditableField(label = stringResource(R.string.Nombre), newNombre) {
                        newNombre = it
                    }
                    EditableField(
                        label = stringResource(R.string.Apellido),
                        value = newApellido
                    ) { newApellido = it }
                    EditableField(
                        label = stringResource(R.string.DNI),
                        newDni,
                        isError = isValidDNI
                    ) {
                        newDni = it
                        isValidDNI = isValidDni(newDni)
                    }
                    EditableField(
                        label = stringResource(R.string.Email),
                        newEmail,
                        keyboardType = KeyboardType.Email,
                        isError = isValidMail
                    ) {
                        newEmail = it
                        isValidMail = isValidDni(newEmail)
                    }
                    EditableField(label = stringResource(R.string.Rol), newRol) { newRol = it }
                    EditableField(
                        label = stringResource(R.string.Credito),
                        newCredito,
                        keyboardType = KeyboardType.Decimal
                    ) { newCredito = it }
                    EditableField(label = stringResource(R.string.Contrasenia), newPassword) {
                        newPassword = it
                    }
                    EditableField(
                        label = stringResource(R.string.Telefono),
                        newTelefono,
                        keyboardType = KeyboardType.Phone,
                        isError = isValidTelefono
                    ) {
                        newTelefono = it
                        isValidTelefono = isValidPhone(newTelefono)
                    }

                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                onUpdate(
                                    usu.id_usuario,
                                    UsuarioUpdate(
                                        nombre = newNombre,
                                        apellido = newApellido,
                                        email = newEmail,
                                        dni = newDni,
                                        password = newPassword,
                                        credito = 0,
                                        rol = newRol,
                                        telefono = newTelefono.toInt()
                                    )
                                )
                                newPassword = ""
                            }, colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(
                                stringResource(R.string.Modificar),
                                color = MaterialTheme.colorScheme.surface
                            )
                        }
                        if (usu.activa == 0) {
                            Button(
                                enabled = !isCurrentUser,
                                onClick = {
                                    onActivate(
                                        usu.id_usuario,
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.secondary
                                )
                            ) {
                                Text(
                                    stringResource(R.string.Desactivar),
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                            }
                        } else if (usu.activa == 1) {
                            Button(
                                enabled = !isCurrentUser,
                                onClick = {
                                    onActivate(
                                        usu.id_usuario,
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.secondary
                                )
                            ) {
                                Text(
                                    stringResource(R.string.Activar),
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EditableField(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        isError = isError,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary
        )
    )
}