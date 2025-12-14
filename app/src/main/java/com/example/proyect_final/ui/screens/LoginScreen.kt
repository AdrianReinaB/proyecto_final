package com.example.proyect_final.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyect_final.R
import com.example.proyect_final.data.Usuario
import com.example.proyect_final.viewModel.LoginViewModel

@Composable
fun LoginScreen(onLoginSuccess: (Usuario) -> Unit,loginViewModel: LoginViewModel, navigateToRegister: () -> Unit) {
    val context = LocalContext.current
    var loginEmail by remember { mutableStateOf("") }
    var loginPass by remember { mutableStateOf("") }

    val user by loginViewModel.user.collectAsState()
    val userFail by loginViewModel.userSuccess.collectAsState()

    LaunchedEffect(userFail) {
        when(userFail) {
            true -> Unit
            false -> Toast.makeText(context, "Error en el login", Toast.LENGTH_SHORT).show()
            null -> Unit
        }
    }

    LaunchedEffect(user) {
        user?.let {
            onLoginSuccess(it)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(20.dp)
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = stringResource(R.string.Login), fontSize = 30.sp,
                    color = MaterialTheme.colorScheme.primary)
                OutlinedTextField(
                    value = loginEmail,
                    onValueChange = { loginEmail = it },
                    label = { Text(text = stringResource(R.string.Email)) },
                    singleLine = true
                )
                OutlinedTextField(
                    value = loginPass,
                    onValueChange = { loginPass = it },
                    label = { Text(text = stringResource(R.string.Contrasenia)) },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                )
                Button(
                    onClick = {
                        loginViewModel.loginUser(loginEmail, loginPass)
                    },
                    enabled = loginEmail.isNotBlank()
                            && loginPass.isNotBlank(),
                    modifier = Modifier.padding(5.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = stringResource(R.string.Entrar))
                }
                Button(onClick = { navigateToRegister() }, colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )) {
                    Text(stringResource(R.string.Registrarse))
                }
            }
        }
    }
}

