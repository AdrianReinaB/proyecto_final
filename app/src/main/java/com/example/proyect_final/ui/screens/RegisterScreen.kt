package com.example.proyect_final.ui.screens

import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.proyect_final.data.UsuarioRegister
import com.example.proyect_final.data.Usuario
import com.example.proyect_final.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun RegisterScreen() {

    var registerName by remember { mutableStateOf("") }
    var registerLastName by remember { mutableStateOf("") }
    var registerEmail by remember { mutableStateOf("") }
    var registerPass by remember { mutableStateOf("") }
    var registerPhone by remember { mutableStateOf("") }

    val context = LocalContext.current


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
            Button(onClick = {
                RetrofitClient.apiService.postRegister(
                    UsuarioRegister(
                        nombre = registerName,
                        apellido = registerLastName,
                        email = registerEmail,
                        password = registerPass,
                        telefono = registerPhone.toInt()
                    )
                ).enqueue(object : Callback<Usuario> {
                    override fun onResponse(
                        call: Call<Usuario>,
                        response: Response<Usuario>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(context, "El registro tuvo exito", Toast.LENGTH_SHORT).show()
                            Log.d("Registro existoso", "Registro satisfactorio")
                        } else {
                            Log.d("Registro", "Registro no hecho: ${response.code()}")
                        }
                    }
                    override fun onFailure(call: Call<Usuario>, t: Throwable) {
                        Log.d("Registro fail", t.message.toString())
                    }
                })
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)) {
                Text("Registrarte")
            }
        }
    }
}