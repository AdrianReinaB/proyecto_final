package com.example.proyect_final.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RegisterScreen() {

    var registerName by remember { mutableStateOf("") }
    var registerLastName by remember { mutableStateOf("") }
    var registerEmail by remember { mutableStateOf("") }
    var registerPass by remember { mutableStateOf("") }
    var registerPhone by remember { mutableStateOf("") }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(10.dp), contentAlignment = Alignment.Center) {
        Column {
            Row {
                OutlinedTextField(
                    value = registerName,
                    onValueChange = { registerName = it },
                    label = { Text("Nombre") },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                )
                OutlinedTextField(
                    value = registerLastName,
                    onValueChange = { registerLastName = it },
                    label = { Text("Apellidos") },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                )
            }
            Column {
                OutlinedTextField(
                    value = registerPhone,
                    onValueChange = { registerPhone = it },
                    label = { Text("telefono") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
                OutlinedTextField(
                    value = registerEmail,
                    onValueChange = { registerEmail = it },
                    label = { Text("Email*") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
                OutlinedTextField(
                    value = registerPass,
                    onValueChange = { registerPass = it },
                    label = { Text("Contraseña*") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
            }
            Button(onClick = {}, modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)) {
                Text("Registrarte")
            }
        }
    }
}