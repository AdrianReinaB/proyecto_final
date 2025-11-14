package com.example.proyect_final.ui.screens

import android.util.Log
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyect_final.data.Usuario
import com.example.proyect_final.data.UsuarioLogin
import com.example.proyect_final.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun LoginScreen(onLoginSuccess: (Usuario) -> Unit, navigateToRegister: () -> Unit) {
    val context = LocalContext.current
    var loginEmail by remember { mutableStateOf("cliente1@cli.es") }
    var loginPass by remember { mutableStateOf("123456") }

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(20.dp)
        ) {
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
                    onClick = {
                        RetrofitClient.apiService.postData(
                            UsuarioLogin(
                                email = loginEmail,
                                password = loginPass
                            )
                        ).enqueue(object : Callback<Usuario> {
                            override fun onResponse(
                                call: Call<Usuario>,
                                response: Response<Usuario>
                            ) {
                                var body = response.body()
                                if (response.isSuccessful) {
                                    response.body()?.let { usuario ->
                                        onLoginSuccess(usuario)
                                    }
//                                    if (body != null) {
//                                        Log.d("login", "usuario: ${body}")
//                                    }
//                                    if (body != null) {
//                                        navigateToHome()
//                                    }
                                } else {
                                    Log.d(
                                        "Login mal",
                                        "Credenciales incorrectas: ${response.code()}"
                                    )
                                }
                            }

                            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                                Log.d("Login fail", t.message.toString())
                                Toast.makeText(context, "Ups, paso algo malo", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        })
                    },
                    enabled = loginEmail.isNotBlank()
                            && loginPass.isNotBlank(),
                    modifier = Modifier.padding(5.dp)
                ) {
                    Text(text = "Enviar")
                }
                TextButton(onClick = {}) {
                    Text("¿Olvidaste la contraseña?")
                }
                Button(onClick = { navigateToRegister() }) {
                    Text("Registrarse")
                }
            }
        }
    }
}