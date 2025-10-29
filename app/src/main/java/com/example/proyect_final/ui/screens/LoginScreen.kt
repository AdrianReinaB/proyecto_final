package com.example.proyect_final.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen() {

    var loginEmail by remember { mutableStateOf("") }
    var loginPass by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color.LightGray)
            .padding(20.dp)){
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Login", fontSize = 30.sp)
                OutlinedTextField(
                    value = loginEmail,
                    onValueChange = { loginEmail = it },
                    label = { Text(text = "email") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = loginPass,
                    onValueChange = { loginPass = it },
                    label = { Text(text = "password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                )
                Button(
                    onClick = {},
                    enabled = loginEmail.isNotBlank()
                            && loginPass.isNotBlank(), modifier = Modifier.padding(5.dp)
                ) {
                    Text(text = "Enviar")
                }
                TextButton (onClick = {}){
                    Text("¿Olvidaste la contraseña?")
                }
                Button(onClick = {}) {
                    Text("Registrarse")
                }
            }
        }
    }
}