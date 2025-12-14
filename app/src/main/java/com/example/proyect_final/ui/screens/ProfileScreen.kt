package com.example.proyect_final.ui.screens

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyect_final.R
import com.example.proyect_final.data.UsuarioUpdate
import com.example.proyect_final.utility.isValidDni
import com.example.proyect_final.utility.isValidEmail
import com.example.proyect_final.utility.isValidPhone
import com.example.proyect_final.utility.toDate
import com.example.proyect_final.utility.toDateString
import com.example.proyect_final.viewModel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileScreen(
    userViewModel: UserViewModel,
    navigateToMovie: (Int) -> Unit,
) {
    var isEditing by remember { mutableStateOf(false) }
    val opinions by userViewModel.opinionForUser.collectAsState()
    val rents by userViewModel.rentUser.collectAsState()
    val us = userViewModel.currentUser.value
    val context = LocalContext.current

    var nombre by remember { mutableStateOf(us?.nombre) }
    var apellido by remember { mutableStateOf(us?.apellido) }
    var telefono by remember { mutableStateOf(us?.telefono ?: "") }
    var email by remember { mutableStateOf(us?.email) }
    var password by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf(us?.dni) }


    var isValidDNI by remember { mutableStateOf(false) }
    var isValidMail by remember { mutableStateOf(false) }
    var isValidTelefono by remember { mutableStateOf(false) }

    val allValid =
        (nombre?.isNotBlank() == true && nombre!!.all { it.isLetter() || it.isWhitespace() }) &&
                (apellido?.isNotBlank() == true && apellido!!.all { it.isLetter() || it.isWhitespace() }) &&
                dni?.let { isValidDni(it) } == true &&
                email?.let { isValidEmail(it) } == true &&
                isValidPhone(telefono) &&
                (password.isEmpty() || password.length >= 8)



    LaunchedEffect(Unit) {
        us?.let {
            userViewModel.reloadCurrentUser()
            userViewModel.loadOpinionForUser(it.id_usuario)
            userViewModel.loadRentUserMovie(it.id_usuario)
        }

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Text(
                text = stringResource(R.string.Pefil_usuario),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp),
                color = MaterialTheme.colorScheme.primary
            )
            IconButton(onClick = { isEditing = !isEditing }) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "", tint = MaterialTheme.colorScheme.secondary)
            }
        }

        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                    EditableField(
                        stringResource(R.string.Nombre),
                        nombre!!,
                        isEditing,
                        isError = !nombre!!.all { name -> name.isLetter() || name.isWhitespace()} || nombre!!.isEmpty()
                    ) { nombre = it }

                    EditableField(
                        stringResource(R.string.Apellido),
                        apellido!!,
                        isEditing,
                        isError = !apellido!!.all { lastName -> lastName.isLetter() || lastName.isWhitespace() } || apellido!!.isEmpty()
                    ) { apellido = it }

                EditableField(
                    stringResource(R.string.Telefono),
                    telefono,
                    isEditing,
                    isError = !isValidTelefono && telefono.isNotEmpty(),
                    keyboardType = KeyboardType.Number
                ) { telefono = it
                    isValidTelefono = isValidPhone(telefono)
                }
                    EditableField(
                        stringResource(R.string.DNI),
                        dni!!,
                        isEditing,
                        isError = !isValidDNI && dni!!.isNotEmpty(),
                        )
                    {
                        dni = it
                        isValidDNI = isValidDni(dni!!)
                    }

                    EditableField(stringResource(R.string.Email),
                        email!!,
                        isEditing,
                        isError = !isValidMail && email!!.isNotEmpty()
                    )
                    {
                        email = it
                        isValidMail = isValidEmail(email!!)
                    }

                    EditableField(
                        stringResource(R.string.Contrasenia),
                        password,
                        isEditing,
                        isPassword = true,
                        isError = password.isNotEmpty() && password.length < 8
                    ) { password = it }


                if (us != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.Credito) + ": " + us.credito + "€",
                            modifier = Modifier.padding(top = 12.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                }

                if (isEditing) {
                    Button(
                        onClick = {
                            if (us != null) {
                               if (allValid){
                                    userViewModel.updateUser(
                                        us.id_usuario,
                                        UsuarioUpdate(
                                            nombre = nombre!!,
                                            apellido = apellido!!,
                                            email = email!!,
                                            password = password,
                                            telefono = telefono.toInt(),
                                            dni = dni!!
                                        )
                                    )
                                    Toast.makeText(context, "Usuario actualizado", Toast.LENGTH_SHORT).show()
                                }else{
                                    Toast.makeText(context, "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
                                    nombre= us.nombre
                                    apellido = us.apellido
                                    dni = us.dni
                                    email = us.email
                                    password = us.password
                                    telefono = us.telefono
                                }

                            }
                            isEditing = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            stringResource(R.string.Guardar_cambios),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            stringResource(R.string.Alquileres), fontSize = 22.sp, fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        if (rents.isNullOrEmpty()) {
            Text(
                stringResource(R.string.No_alquiler),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp)
            ) {
                    items(rents!!) { alquiler ->
                        if (alquiler.estado == "activo") {
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Text(
                                    alquiler.titulo,
                                    modifier = Modifier.padding(12.dp),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    stringResource(R.string.Vencimiento) + ": \t" + toDateString(
                                        toDate(
                                            alquiler.fecha_fin
                                        )
                                    ),
                                    modifier = Modifier.padding(12.dp),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                }

            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            stringResource(R.string.Comentarios), fontSize = 22.sp, fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        if (opinions.isNullOrEmpty()) {
            Text(
                stringResource(R.string.No_comentarios),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp)
            ) {
                    items(opinions!!) { opi ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            onClick = { navigateToMovie(opi.pelicula_id_pelicula) },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                Text(opi.titulo, color = MaterialTheme.colorScheme.onSurface)
                                Text(
                                    toDateString(toDate(opi.fecha)),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Text(
                                text = opi.comentario,
                                modifier = Modifier.padding(12.dp),
                                color = MaterialTheme.colorScheme.onSurface
                            )
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
    isEditing: Boolean,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    isError: Boolean = false,
    onValueChange: (String) -> Unit
) {
    if (isEditing) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            isError = isError,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
        )
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
            Text(value, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}
