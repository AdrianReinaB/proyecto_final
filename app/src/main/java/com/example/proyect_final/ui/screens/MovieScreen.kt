package com.example.proyect_final.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import java.util.Date
import java.util.Locale

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieScreen(
    id: Int,
    userViewModel: UserViewModel,
    movieViewModel: MovieViewModel = viewModel()
) {

    val movie by movieViewModel.movie.collectAsState()
    val opinions by movieViewModel.opinions.collectAsState()
    val users by movieViewModel.users.collectAsState()
    val currentUser = userViewModel.currentUser.value
    var comentario by remember { mutableStateOf("") }
    var mostrarComentario by remember { mutableStateOf(false) }
    var refreshComments by remember { mutableIntStateOf(0) }
    var rent by remember { mutableStateOf(false) }

    LaunchedEffect(id) {
        movieViewModel.loadMovie(id)
        movieViewModel.loadOpinions(id)
    }

    LaunchedEffect(refreshComments) {
        movieViewModel.loadOpinions(id)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            AsyncImage(
                model = movie?.imagen
                    ?: "https://i.pinimg.com/originals/82/d2/5b/82d25b943e9c7a1f9e801f498461d4f6.jpg",
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(bottom = 16.dp)
            )
        }
        item {
            movie?.let {
                Text(
                    text = it.titulo,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
        item {
            movie?.let {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(text = "Año: ${it.año}", fontSize = 16.sp)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(text = "Género: ${it.genero}", fontSize = 16.sp)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(text = "Edad: ${it.clasificacion_edad}", fontSize = 16.sp)
                }
            }
        }
        item {
            movie?.let {
                Text(
                    text = it.sinopsis,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }
        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 25.dp, end = 25.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                movie?.let {
                    Button(onClick = { rent = !rent }) {
                        Text("Alquilar", fontWeight = FontWeight.Bold)
                    }
                }
                Button(onClick = { mostrarComentario = !mostrarComentario }) {
                    Text(
                        if (mostrarComentario) "Cancelar" else "Comentar",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        if (mostrarComentario) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = comentario,
                    onValueChange = { comentario = it },
                    placeholder = { Text("Da tu opinión...") },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 5
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    movieViewModel.postOpinion(
                        RegisterValoracion(
                            puntuacion = 1,
                            comentario = comentario,
                            usuario_id_usuario = currentUser!!.id_usuario,
                            pelicula_id_pelicula = id
                        )
                    )
                    Log.d("Comentario", "Comentario enviado: $comentario")
                    comentario = ""
                    mostrarComentario = false
                    refreshComments++
                }) {
                    Text("Enviar")
                }
            }
        }

        if (rent) {
            item {
                if (currentUser != null) {
                    DatePickerScreen(id, currentUser.id_usuario)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Comentarios:",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        items(opinions) { valoracion ->
            val user = users[valoracion.usuario_id_usuario]
            user?.let {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = it.nombre
                )
            }
            Text(
                text = valoracion.comentario,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerScreen(id_movie: Int, user_id: Int) {
    val today =  Date()
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    var selectedDate by remember { mutableStateOf<Date?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()

    Card(modifier = Modifier.padding(10.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Fecha de inicio: ${formatter.format(today)}")
            Spacer(modifier = Modifier.height(16.dp))
            Text("Fecha final: ${selectedDate?.let { formatter.format(it) } ?: "Ninguna"}")

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = { showDatePicker = true }) {
                Text("Seleccionar fecha")
            }

            Button(onClick = {
                RetrofitClient.apiService.productRent(
                    RentMovie(
                        fecha_inicio = formatter.format(today),
                        fecha_fin = selectedDate?.let { formatter.format(it) } ?: "Ninguna",
                        pelicula_id_pelicula = id_movie,
                        usuario_id_usuario = user_id
                    )
                ).enqueue(object : Callback<Producto> {
                    override fun onResponse(
                        call: Call<Producto>,
                        response: Response<Producto>
                    ) {
                        if (response.isSuccessful) {
                            Log.d(
                                "Alquiler",
                                "Alquiler de pelicula satisfactorio"
                            )
                        } else {
                            val errorBody = response.errorBody()?.string()
                            Log.d(
                                "Alquiler",
                                "Alquiler no hecho: ${response.code()}, $errorBody"
                            )
                        }
                    }

                    override fun onFailure(call: Call<Producto>, t: Throwable) {
                        Log.d("Alquiler fail", t.message.toString())
                    }
                })
            }) {
                Text("Alquilar pelicula")
            }
            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let {
                                selectedDate = Date(it)
                            }
                            showDatePicker = false
                        }) { Text("Aceptar") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Cancelar")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }
        }
    }
}
