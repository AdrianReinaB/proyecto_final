package com.example.proyect_final.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyect_final.data.RentMovieUser
import com.example.proyect_final.data.Usuario
import com.example.proyect_final.data.UsuarioRegister
import com.example.proyect_final.data.ValoracionUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun ProfileScreen(
    userViewModel: UserViewModel,
    navigateToMovie: (Int) -> Unit,
) {
    var isEditing by remember { mutableStateOf(false) }
    var opinions by remember { mutableStateOf<List<ValoracionUser>>(emptyList()) }
    var rents by remember { mutableStateOf<List<RentMovieUser>>(emptyList()) }
    val user = userViewModel.currentUser.value

    var nombre by remember { mutableStateOf(user?.nombre) }
    var apellido by remember { mutableStateOf(user?.apellido) }
    var telefono by remember { mutableStateOf(user?.telefono ?: "") }
    var email by remember { mutableStateOf(user?.email) }
    var password by remember { mutableStateOf(user?.password) }

    LaunchedEffect(user) {
        user?.let {
            RetrofitClient.apiService.getOpinionUser(it.id_usuario)
                .enqueue(object : Callback<List<ValoracionUser>> {
                    override fun onResponse(
                        call: Call<List<ValoracionUser>>,
                        response: Response<List<ValoracionUser>>
                    ) {
                        if (response.isSuccessful) {
                            opinions = response.body() ?: emptyList()
                        } else {
                            Log.d("Peliculas", "Error ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<List<ValoracionUser>>, t: Throwable) {
                        Log.e("Peliculas", "Error: ${t.message}")
                    }
                })
            RetrofitClient.apiService.getRentUser(it.id_usuario)
                .enqueue(object : Callback<List<RentMovieUser>> {
                    override fun onResponse(
                        call: Call<List<RentMovieUser>>,
                        response: Response<List<RentMovieUser>>
                    ) {
                        if (response.isSuccessful) {
                            rents = response.body() ?: emptyList()
                        } else {
                            Log.d("Peliculas", "Error ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<List<RentMovieUser>>, t: Throwable) {
                        Log.e("Peliculas", "Error: ${t.message}")
                    }
                })
        }

    }

    Log.d("User_id", user.toString())
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Text(
                text = "Perfil de Usuario",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            IconButton(onClick = { isEditing = !isEditing }) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "")
            }
        }

        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                nombre?.let { EditableField("Nombre", it, isEditing) { nombre = it } }
                apellido?.let { EditableField("Apellido", it, isEditing) { apellido = it } }
                EditableField(
                    "Teléfono", telefono, isEditing, keyboardType = KeyboardType.Number
                ) { telefono = it }
                email?.let { EditableField("Email", it, isEditing) { email = it } }
                password?.let {
                    EditableField(
                        "Contraseña", it, isEditing, isPassword = true
                    ) { password = it }
                }

                if (user != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Crédito: ${user.credito} €",
                            modifier = Modifier.padding(top = 12.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
//                        IconButton(onClick = {}) {
//                            Icon(imageVector = Icons.Default.Add, contentDescription = "")
//                        }
                    }

                }

                if (isEditing) {
                    Button(
                        onClick = {
                            if (user != null) {
                                RetrofitClient.apiService.updateUser(
                                    user.id_usuario,
                                    UsuarioRegister(
                                        nombre = nombre!!,
                                        apellido = apellido!!,
                                        email = email!!,
                                        password = password!!,
                                        telefono = telefono.toInt()
                                    )
                                ).enqueue(object : Callback<Usuario> {
                                    override fun onResponse(
                                        call: Call<Usuario>,
                                        response: Response<Usuario>
                                    ) {
                                        if (response.isSuccessful) {
                                            val updatedUser = response.body()
                                            if (updatedUser != null) {
                                                userViewModel.setUser(updatedUser)
                                            }
                                            Log.d(
                                                "Update user",
                                                "Cambio usuario satisfactorio"
                                            )
                                        } else {
                                            val errorBody = response.errorBody()?.string()
                                            Log.d(
                                                "Update user else",
                                                "Update no hecho: ${response.code()}, $errorBody"
                                            )
                                        }
                                    }

                                    override fun onFailure(call: Call<Usuario>, t: Throwable) {
                                        Log.d("Update fail", t.message.toString())
                                    }
                                })
                            } else {
                                Log.d("no actualizado", "no se pudo determinar el id del usuario")
                            }
                            isEditing = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    ) {
                        Text("Guardar cambios")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Alquileres", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        if (rents.isEmpty()) {
            Text(
                "No tienes alquileres activos",
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

                items(rents) { alquiler ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(alquiler.titulo, modifier = Modifier.padding(12.dp))
                        Text("Vencimiento: \t${alquiler.fecha_fin}", modifier = Modifier.padding(12.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Comentarios", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        if (opinions.isEmpty()) {
            Text(
                "Aún no has hecho comentarios",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            Log.d("Opiniones", opinions.toString())
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp)
            ) {
                items(opinions) { opi ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        onClick = {navigateToMovie(opi.pelicula_id_pelicula)},
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)) {
                            Text(opi.titulo)
                            Text(opi.fecha)
                        }
                        Text(
                            text = opi.comentario,
                            modifier = Modifier.padding(12.dp)
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
    onValueChange: (String) -> Unit
) {
    if (isEditing) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
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
            Text(label, fontWeight = FontWeight.Medium)
            Text(value, color = MaterialTheme.colorScheme.primary)
        }
    }
}
