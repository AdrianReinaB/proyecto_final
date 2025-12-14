package com.example.proyect_final.ui.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.proyect_final.R
import com.example.proyect_final.data.RegisterValoracion
import com.example.proyect_final.utility.toDate
import com.example.proyect_final.utility.toDateString
import com.example.proyect_final.viewModel.MovieViewModel
import com.example.proyect_final.viewModel.UserViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MovieScreen(
    id: Int, userViewModel: UserViewModel, movieViewModel: MovieViewModel = viewModel()
) {

    val movie by movieViewModel.movie.collectAsState()
    val opinions by movieViewModel.opinions.collectAsState()
    val users by movieViewModel.users.collectAsState()
    val delOpinion by movieViewModel.delopinion.collectAsState()
    val currentUser = userViewModel.currentUser.value
    var comentario by remember { mutableStateOf("") }
    var numOp by remember { mutableStateOf(1) }
    var mostrarComentario by remember { mutableStateOf(false) }
    var refreshComments by remember { mutableIntStateOf(0) }
    var rent by remember { mutableStateOf(false) }
    val productRentState by movieViewModel.productRent.collectAsState()

    var expanded by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(id) {
        movieViewModel.loadMovie(id)
        movieViewModel.loadOpinions(id)
    }

    LaunchedEffect(refreshComments) {
        movieViewModel.loadOpinions(id)
    }

    LaunchedEffect(productRentState) {
        when(productRentState) {
            true -> Toast.makeText(context, "Alquiler exitoso", Toast.LENGTH_SHORT).show()
            false -> Toast.makeText(context, "Error en el alquiler", Toast.LENGTH_SHORT).show()
            null -> Unit
        }
    }

    val averageRating = remember(opinions) {
        if (opinions.isNotEmpty()) {
            opinions.map { it.puntuacion }.average()
        } else {
            0.0
        }
    }



    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            AsyncImage(
                model = movie?.imagen,
                contentDescription = "",
                modifier = Modifier.size(180.dp)
            )
        }
        item {
            movie?.let {
                Text(
                    text = it.titulo,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = MaterialTheme.colorScheme.primary
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
                    Text(text = stringResource(R.string.Anio) + ": " +it.anio, fontSize = 16.sp,color = MaterialTheme.colorScheme.onSurface)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(text = stringResource(R.string.Genero)+ ": " +it.genero, fontSize = 16.sp,color = MaterialTheme.colorScheme.onSurface)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(text = stringResource(R.string.Edad_recomendada)+": "+ it.clasificacion_edad, fontSize = 16.sp,color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
        item {
            movie?.let {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.Sinopsis_pelicula), fontSize = 16.sp,color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = it.sinopsis,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 24.dp),color = MaterialTheme.colorScheme.onSurface
                    )
                }

            }
        }
        item {
            movie?.let {
                Text(
                    text = stringResource(R.string.Catalogo)+": "+it.disponibilidad,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 24.dp),color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        item {
            Row {
                repeat(5) { index ->
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = if (index < averageRating.roundToInt())
                            MaterialTheme.colorScheme.secondary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }


        item {
            HorizontalDivider(Modifier.padding(vertical = 10.dp))
            Row(

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 25.dp, end = 25.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                movie?.let {
                    Button(
                        onClick = { rent = !rent }, enabled = movie!!.disponibilidad == "disponible",
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(stringResource(R.string.Alquilar), fontWeight = FontWeight.Bold,color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
                Button(onClick = { mostrarComentario = !mostrarComentario },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )) {
                    Text(
                        if (mostrarComentario) stringResource(R.string.Cancelar) else stringResource(R.string.Comentar),
                        fontWeight = FontWeight.Bold,color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        }

        if (mostrarComentario) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                TextField(value = comentario,
                    onValueChange = { comentario = it },
                    placeholder = { Text(stringResource(R.string.Da_opinion),
                        color = MaterialTheme.colorScheme.onSurface) },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 5,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = numOp.toString(),color = MaterialTheme.colorScheme.primary)
                    if (!expanded) {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = ""
                            )
                        }
                    } else {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowUp, contentDescription = ""
                            )
                        }
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        for (i in 1..5) {
                            DropdownMenuItem(
                                text = { Text(i.toString(),color = MaterialTheme.colorScheme.primary) },
                                onClick = {
                                    numOp = i
                                    expanded = !expanded
                                },
                            )
                        }

                    }
                }

                Button(onClick = {
                    movieViewModel.postOpinion(
                        RegisterValoracion(
                            puntuacion = numOp,
                            comentario = comentario,
                            usuario_id_usuario = currentUser!!.id_usuario,
                            pelicula_id_pelicula = id
                        )
                    )
                    comentario = ""
                    mostrarComentario = false
                    refreshComments++
                }) {
                    Text(stringResource(R.string.Enviar),color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }

        if (rent) {
            item {
                if (currentUser != null) {
                    DatePickerScreen(id, currentUser.id_usuario, viewModel())
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = stringResource(R.string.Comentarios), fontSize = 20.sp, fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        items(opinions) { op ->
            val user = users[op.usuario_id_usuario]
            Card(modifier = Modifier.padding(vertical = 10.dp),colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )) {
                Column(modifier = Modifier.padding(12.dp)) {
                    user?.let {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = it.nombre, modifier = Modifier.weight(1f),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = toDateString(toDate(op.fecha)),
                                modifier = Modifier.padding(end = 10.dp),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (currentUser?.id_usuario == op.usuario_id_usuario) {
                                IconButton(modifier = Modifier.size(20.dp), onClick = {
                                    movieViewModel.delOpinion(op.id_valoracion)
                                    refreshComments++
                                    Toast.makeText(
                                        context, delOpinion.toString(), Toast.LENGTH_LONG
                                    ).show()
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete, contentDescription = ""
                                    )
                                }
                            }
                        }
                    }
                    Row {
                        Text(
                            text = op.comentario,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(text = stringResource(R.string.Puntuacion)+": "+op.puntuacion,
                            color = MaterialTheme.colorScheme.onSurface)
                    }

                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerScreen(id_movie: Int, user_id: Int, movieViewModel: MovieViewModel) {
    val today = LocalDate.now()

    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()

    Card(modifier = Modifier.padding(10.dp),colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface
    )) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Fecha de inicio: ${toDateString(today)}")
            Spacer(modifier = Modifier.height(16.dp))
            Text("Fecha final: ${selectedDate?.let { toDateString(it) } ?: stringResource(R.string.Ninguna)}")

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = { showDatePicker = true }, colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )) {
                Text(stringResource(R.string.Seleccion_fecha))
            }

            Button(onClick = {
                val finalDate = selectedDate ?: today
                val fechaFinDate: Date = Date.from(finalDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
                movieViewModel.rentMovie(fechaFinDate, id_movie, user_id)
            },
                enabled = selectedDate == null || !selectedDate!!.isBefore(today),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )) {
                Text(stringResource(R.string.Alquilar_pelicula))
            }
            if (showDatePicker) {
                DatePickerDialog(onDismissRequest = { showDatePicker = false }, confirmButton = {
                    val context = LocalContext.current
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let {
                            val pickDate = Instant.ofEpochMilli(it)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            if (pickDate.isBefore(today)) {
                                Toast.makeText(
                                    context,
                                    "No puedes seleccionar una fecha anterior a hoy",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                selectedDate = pickDate
                            }
                        }
                        showDatePicker = false
                    }) { Text(stringResource(R.string.Aceptar)) }
                }, dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text(stringResource(R.string.Cancelar))
                    }
                }) {
                    DatePicker(state = datePickerState)
                }
            }
        }
    }
}
