package com.example.proyect_final.ui.screens.admin

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.proyect_final.data.UsuarioRent
import com.example.proyect_final.utility.toDate
import com.example.proyect_final.utility.toDateString
import com.example.proyect_final.viewModel.RentMoviesViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserRentList(rentMoviesViewModel: RentMoviesViewModel) {

    val ListUserRents by rentMoviesViewModel.rents.collectAsState()

    var showSearch by remember { mutableStateOf(false) }
    var search by remember { mutableStateOf("") }

    val registerMovieStatus by rentMoviesViewModel.returnMovie.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(registerMovieStatus) {
        when(registerMovieStatus) {
            true -> Toast.makeText(context, "Pelicula devuelta", Toast.LENGTH_SHORT).show()
            false -> Toast.makeText(context, "Error en la operacion", Toast.LENGTH_SHORT).show()
            null -> Unit
        }
    }

    LaunchedEffect(Unit) {
        rentMoviesViewModel.getRents()
    }

    val filteredRents = ListUserRents?.filter {
        val nameUser = it.nombre.contains(search, ignoreCase = true)

        val stateRent = it.estado_a.contains(search, ignoreCase = true)

        nameUser || stateRent
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { showSearch = !showSearch }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurface)
            }
            if (showSearch) {
                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    label = { Text("Buscador") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary)
                )

            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .border(1.dp, MaterialTheme.colorScheme.onSurface)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Nombre", modifier = Modifier
                    .wrapContentWidth()
                    .padding(end = 10.dp), overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                "alquilada", modifier = Modifier
                    .wrapContentWidth()
                    .padding(end = 10.dp), overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                "Finalizacion", modifier = Modifier
                    .wrapContentWidth()
                    .padding(end = 10.dp), overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                "Pelicula", modifier = Modifier.wrapContentWidth(), overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            if (filteredRents != null){
                items(filteredRents) { al ->

                    var showInfo by remember { mutableStateOf(false) }
                    var expanded by remember { mutableStateOf(false) }

                    if (showInfo) {
                        Devolver(
                            userRent = al,
                            onDismiss = { showInfo = false },
                            onConfirm = {id->
                                rentMoviesViewModel.returnMovie(id)
                                showInfo = false
                            }
                        )
                    }
                    val hoy = LocalDate.now()
                    val colorBorder = when {
                        al.estado_pr == "alquilado" && toDate(al.fecha_fin) < hoy ->
                            MaterialTheme.colorScheme.surfaceBright

                        al.estado_pr == "alquilado" && toDate(al.fecha_fin) > hoy ->
                            MaterialTheme.colorScheme.onErrorContainer

                        al.estado_pr == "devuelto" ||
                                al.estado_pr == "disponible" ||
                                al.estado_pr == "inactivo" ->
                            MaterialTheme.colorScheme.secondary

                        else ->
                            MaterialTheme.colorScheme.secondary
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .border(1.dp, colorBorder)
                            .height(50.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { expanded = true },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(al.nombre, modifier = Modifier
                            .wrapContentWidth()
                            .padding(end = 10.dp, start = 10.dp), overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurface)
                        Text(
                            toDateString(toDate(al.fecha_inicio)), modifier = Modifier
                                .wrapContentWidth()
                                .padding(end = 10.dp), overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurface)
                        Text(
                            toDateString(toDate(al.fecha_fin)), modifier = Modifier
                                .wrapContentWidth()
                                .padding(end = 10.dp), overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurface)
                        Text(
                            al.titulo, modifier = Modifier
                                .wrapContentWidth()
                                .padding(end = 10.dp), overflow = TextOverflow.Ellipsis, maxLines = 1,
                            color = MaterialTheme.colorScheme.onSurface)

                        if (al.estado_pr=="alquilado"){
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Devolver pelicula") },
                                    onClick = {
                                        expanded = false
                                        showInfo = true
                                    }
                                )
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
fun Devolver(userRent: UsuarioRent, onDismiss: () -> Unit, onConfirm: (Int) -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = { onConfirm(userRent.id_producto )}) { Text("Devolver", color =
            MaterialTheme.colorScheme.primary) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color =
                    MaterialTheme.colorScheme.secondary)
            }
        },
        title = { Text("¿Devolver pelicula?",
            color = MaterialTheme.colorScheme.onSurface) },
        text = {
            Text("Fecha alquilado: ${toDateString(toDate(userRent.fecha_inicio))}\n" +
                    "Fecha vencimiento: ${toDateString(toDate(userRent.fecha_fin))}",
                color = MaterialTheme.colorScheme.onSurface)
        }
    )
}

