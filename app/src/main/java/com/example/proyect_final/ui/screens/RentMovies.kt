package com.example.proyect_final.ui.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.proyect_final.R
import com.example.proyect_final.data.RentMovieUser
import com.example.proyect_final.utility.toDate
import com.example.proyect_final.utility.toDateString
import com.example.proyect_final.viewModel.RentMoviesViewModel
import com.example.proyect_final.viewModel.UserViewModel
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentMoviesUser(userViewModel: UserViewModel, rentMoviesViewModel: RentMoviesViewModel) {
    val rents by userViewModel.rentUser.collectAsState()
    val movieReturn by rentMoviesViewModel.returnMovie.collectAsState()
    val user = userViewModel.currentUser.value

    var showSearch by remember { mutableStateOf(false) }

    var showActivos by remember { mutableStateOf(false) }
    var showVencidos by remember { mutableStateOf(false) }

    var search by remember { mutableStateOf("") }

    LaunchedEffect(movieReturn) {
        if (movieReturn == true) {
            user?.let {
                userViewModel.loadRentUserMovie(it.id_usuario)
            }
        }
    }

    LaunchedEffect(Unit) {
        user?.let {
            userViewModel.loadRentUserMovie(it.id_usuario)
        }
    }



    val filteredRents = rents?.filter { rent ->
        val titleMatch = rent.titulo.contains(search, ignoreCase = true)

        val dateString = rent.fecha_fin
        val fechaMatch = dateString.contains(search, ignoreCase = true)

        titleMatch || fechaMatch
    }

    val activos = filteredRents?.filter { it.estado == "activo" }
    val vencidos = filteredRents?.filter { it.estado != "activo" }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { showSearch = !showSearch }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "",
                        tint = MaterialTheme.colorScheme.onSurface)
                }
                if (showSearch) {
                    OutlinedTextField(
                        value = search,
                        onValueChange = { search = it },
                        label = { Text(stringResource(R.string.Buscar_pelicula)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clickable { showActivos = !showActivos }) {
                Text(
                    if (showActivos) stringResource(R.string.Alquiler_activo_bottom) else stringResource(
                        R.string.Alquiler_activo_rigth
                    ), color = MaterialTheme.colorScheme.primary
                )
            }

            AnimatedVisibility(visible = showActivos) {
                if (activos != null) {
                    if (activos.isEmpty()) {
                        Text(
                            stringResource(R.string.Sin_alquileres),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(150.dp),
                            contentPadding = PaddingValues(12.dp)
                        ) {
                            items(activos) { movie ->
                                if (user != null) {
                                    RentMovieCard(movie)
                                }
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clickable { showVencidos = !showVencidos }) {
                Text(
                    if (showVencidos) stringResource(R.string.Alquiler_vencido_bottom) else stringResource(
                        R.string.Alquiler_vencido_rigth
                    ), color = MaterialTheme.colorScheme.secondary
                )
            }

            AnimatedVisibility(visible = showVencidos) {
                if (vencidos != null) {
                    if (vencidos.isEmpty()) {
                        Text(
                            stringResource(R.string.Sin_alquiler_vencido),
                            modifier = Modifier.padding(8.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(150.dp),
                            contentPadding = PaddingValues(12.dp)
                        ) {
                            items(vencidos) { movie ->
                                RentMovieCard(movie, showReturnButton = false)
                            }
                        }
                    }
                }
            }
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentMovieCard(
    pelis: RentMovieUser,
    showReturnButton: Boolean = true,
    rentMoviesViewModel: RentMoviesViewModel = viewModel(),
) {
    val hoy = LocalDate.now()
    val diasDiferencia = ChronoUnit.DAYS.between(hoy, toDate(pelis.fecha_fin))
    val registerMovieStatus by rentMoviesViewModel.returnMovie.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(registerMovieStatus) {
        when(registerMovieStatus) {
            true -> Toast.makeText(context, "Pelicula devuelta", Toast.LENGTH_SHORT).show()
            false -> Toast.makeText(context, "Error en la operacion", Toast.LENGTH_SHORT).show()
            null -> Unit
        }
    }

    val colorBorder = when {
        diasDiferencia in 0..3 && pelis.estado == "activo" -> colorResource(R.color.warningDark)
        diasDiferencia < 0 && pelis.estado == "activo" -> colorResource(R.color.alertLight)
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(3.dp))
            .padding(5.dp)
            .fillMaxSize()
            .background(colorBorder),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(10.dp)
        ) {
            AsyncImage(
                model = pelis.imagen,
                contentDescription = ""
            )
            Text(
                pelis.titulo,
                modifier = Modifier.padding(5.dp),
                fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                stringResource(R.string.Vencimiento) + "\n" + toDateString(toDate(pelis.fecha_fin)),
                modifier = Modifier.padding(5.dp),
                fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface
            )

            if (showReturnButton) {
                Button(
                    onClick = {
                        rentMoviesViewModel.returnMovie(pelis.id_producto)
                    }, colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        stringResource(R.string.Devolver),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}