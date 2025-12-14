package com.example.proyect_final.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import com.example.proyect_final.R
import com.example.proyect_final.utility.isValidDni
import com.example.proyect_final.utility.isValidEmail
import com.example.proyect_final.utility.isValidPhone
import com.example.proyect_final.viewModel.LoginViewModel

@Composable
fun RegisterScreen(loginViewModel: LoginViewModel) {

    var registerName by remember { mutableStateOf("") }
    var registerLastName by remember { mutableStateOf("") }
    var registerDni by remember { mutableStateOf("") }
    var registerEmail by remember { mutableStateOf("") }
    var registerPass by remember { mutableStateOf("") }
    var registerPhone by remember { mutableStateOf("") }

    val registerState by loginViewModel.registerSuccess.collectAsState()
    val context = LocalContext.current

    var isValidDNI by remember { mutableStateOf(false) }
    var isValidMail by remember { mutableStateOf(false) }
    var isValidTelefono by remember { mutableStateOf(false) }

    val allValid = registerName.isNotBlank() &&
            registerName.all { it.isLetter() || it.isWhitespace() } &&
            registerLastName.isNotBlank() &&
            registerLastName.all { it.isLetter() || it.isWhitespace()  } &&
            isValidDNI &&
            isValidMail &&
            isValidTelefono &&
            registerPass.length >= 8

    LaunchedEffect(registerState) {
        when(registerState) {
            true -> Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
            false -> Toast.makeText(context, "Error en el registro", Toast.LENGTH_SHORT).show()
            null -> Unit
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(10.dp), contentAlignment = Alignment.Center) {
        Column {
            Row {
                OutlinedTextField(
                    value = registerName,
                    onValueChange = { registerName = it },
                    isError = !registerName.all { it.isLetter() || it.isWhitespace() },
                    label = { Text(stringResource(R.string.Nombre)) },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary)
                )
                OutlinedTextField(
                    value = registerLastName,
                    onValueChange = { registerLastName = it },
                    isError = !registerLastName.all { it.isLetter() || it.isWhitespace()  },
                    label = { Text(stringResource(R.string.Apellido)) },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary)
                )
            }
            Column {
                OutlinedTextField(
                    value = registerDni,
                    onValueChange = {
                        registerDni = it.uppercase()
                        isValidDNI = isValidDni(registerDni)
                    },
                    label = { Text(stringResource(R.string.DNI)) },
                    singleLine = true,
                    isError = !isValidDNI && registerDni.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary)
                )
                OutlinedTextField(
                    value = registerPhone,
                    onValueChange = { registerPhone = it
                        isValidTelefono = isValidPhone(registerPhone)},
                    label = { Text(stringResource(R.string.Telefono)) },
                    isError = !isValidTelefono && registerPhone.isNotEmpty(),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary)
                )
                OutlinedTextField(
                    value = registerEmail,
                    onValueChange = { registerEmail = it
                        isValidMail = isValidEmail(registerEmail)
                    },
                    isError = !isValidMail && registerEmail.isNotEmpty(),
                    label = { Text(stringResource(R.string.Email)) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary)
                )
                OutlinedTextField(
                    value = registerPass,
                    onValueChange = { registerPass = it },
                    label = { Text(stringResource(R.string.Contrasenia)) },
                    isError = registerPass.isEmpty() || registerPass.length < 8,
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary)
                )
            }
            Button(onClick = {
                if (allValid){
                    loginViewModel.registerUser(registerName, registerLastName, registerDni, registerEmail, registerPass, registerPhone)
                    registerName=""
                    registerLastName= ""
                    registerDni=""
                    registerEmail=""
                    registerPass=""
                    registerPhone=""
                }else{
                    Toast.makeText(context, "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
                }

            }, modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )) {
                Text(stringResource(R.string.Registrarse))
            }
        }
    }
}