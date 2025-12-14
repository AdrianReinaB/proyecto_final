package com.example.proyect_final.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.proyect_final.R
import com.example.proyect_final.data.ListProducts
import com.example.proyect_final.data.Pelicula
import com.example.proyect_final.viewModel.ListMoviesViewModel

@Composable
fun ListMoviesScreen(listMoviesViewModel: ListMoviesViewModel) {

    val listMovies by listMoviesViewModel.listProduct.collectAsState()

    var showSearch by remember { mutableStateOf(false) }
    var search by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        listMoviesViewModel.listProduct()
    }

    val filteredRents = listMovies?.filter {
        it.titulo.contains(search, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { showSearch = !showSearch }) {
                Icon(
                    imageVector = Icons.Default.Search, contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurface
                )
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
                stringResource(R.string.Nombre),
                modifier = Modifier.weight(1f),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                stringResource(R.string.Total),
                modifier = Modifier.weight(1f),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                stringResource(R.string.Disponibles),
                modifier = Modifier.weight(1f),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                stringResource(R.string.Acciones),
                modifier = Modifier.weight(1f),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            if (filteredRents != null){
                items(filteredRents) { pelicula ->

                    var showInfo by remember { mutableStateOf(false) }
                    var showDelete by remember { mutableStateOf(false) }
                    var showEdit by remember { mutableStateOf(false) }
                    var showTotal by remember { mutableStateOf(false) }
                    var expanded by remember { mutableStateOf(false) }

                    if (showInfo) {
                        InfoDialog(
                            pelicula = pelicula,
                            onDismiss = { showInfo = false }
                        )
                    }

                    if (showDelete) {
                        if (pelicula.activa == 1) {
                            DeleteDialog(
                                pelicula = pelicula,
                                onConfirm = {
                                    listMoviesViewModel.delProduct(pelicula.id_pelicula)
                                    showDelete = false
                                },
                                onDismiss = { showDelete = false }
                            )
                        } else{
                            DeleteDialog(
                                pelicula = pelicula,
                                onConfirm = {
                                    listMoviesViewModel.actProduct(pelicula.id_pelicula)
                                    showDelete = false
                                },
                                onDismiss = { showDelete = false }
                            )
                        }
                    }

                    if (showEdit) {
                        EditDialog(
                            pelicula = pelicula,
                            onConfirm = { editMovie ->
                                listMoviesViewModel.editMovie(editMovie.id_pelicula, editMovie)
                                showEdit = false
                            },
                            onDismiss = { showEdit = false }
                        )
                    }

                    if (showTotal) {
                        ChangeProduct(pelicula = pelicula,
                            onConfirm = { total ->
                                listMoviesViewModel.updateTotalProduct(pelicula.id_pelicula, total)
                                showTotal = false
                            },
                            onDismiss = { showTotal = false })
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .border(1.dp, if (pelicula.activa == 0) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.surfaceBright)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(pelicula.titulo, modifier = Modifier.weight(2f).padding(start = 4.dp),
                            color = MaterialTheme.colorScheme.onSurface)
                        Text("${pelicula.total_productos}", modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.onSurface)
                        Text("${pelicula.disponibles}", modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.onSurface)

                        Box {
                            IconButton(onClick = { expanded = true }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Info") },
                                    onClick = {
                                        expanded = false
                                        showInfo = true
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Editar") },
                                    onClick = {
                                        expanded = false
                                        showEdit = true
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Cantidad") },
                                    onClick = {
                                        expanded = false
                                        showTotal = true
                                    }
                                )
                                DropdownMenuItem(
                                    text = { if (pelicula.activa == 1) Text("Desactivar") else Text("Activar") },
                                    onClick = {
                                        expanded = false
                                        showDelete = true
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

@Composable
fun InfoDialog(pelicula: ListProducts, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cerrar",
                color = MaterialTheme.colorScheme.primary) }
        },
        title = { Text("Información de la película",
            color = MaterialTheme.colorScheme.onSurface) },
        text = {
            Column {
                Text("Título: ${pelicula.titulo}",
                    color = MaterialTheme.colorScheme.onSurface)
                Text("Año: ${pelicula.anio}",
                    color = MaterialTheme.colorScheme.onSurface)
                Text("Genero: ${pelicula.genero}",
                    color = MaterialTheme.colorScheme.onSurface)
                Text("Sinopsis: ${pelicula.sinopsis}",
                    color = MaterialTheme.colorScheme.onSurface)
                Text("Director: ${pelicula.director}",
                    color = MaterialTheme.colorScheme.onSurface)
            }
        }
    )
}

@Composable
fun DeleteDialog(pelicula: ListProducts, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    when(pelicula.activa){
        1-> if (pelicula.total_productos == pelicula.disponibles) {
            AlertDialog(
                onDismissRequest = onDismiss,
                confirmButton = {
                    TextButton(onClick = onConfirm) {
                        Text("Desactivar",
                            color = MaterialTheme.colorScheme.primary)

                    }
                },
                dismissButton = {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar",
                            color = MaterialTheme.colorScheme.secondary)
                    }
                },

                title = { Text("¿Desactivar película?",
                    color = MaterialTheme.colorScheme.onSurface)},
                text = { Text("Esto desactivara la película y no podra ser alquilada.",
                    color = MaterialTheme.colorScheme.onSurface)},
                icon = { Icon(Icons.Default.Warning, contentDescription = "", tint =
                MaterialTheme.colorScheme.onSurface) }
            )
        } else {
            AlertDialog(
                onDismissRequest = onDismiss,
                confirmButton = {
                    TextButton(onClick = onConfirm) {
                        Text("Aceptar")
                    }
                },
                title = { Text("No es posible desactivar la pelicula") },
                text = { Text("Para borrar una pelicula se tiene que tener todas las copias.") },
                icon = { Icon(Icons.Default.Info, contentDescription = "", tint =
                MaterialTheme.colorScheme.onSurface) }
            )
        }
        0-> if (pelicula.total_productos > pelicula.disponibles) {
            AlertDialog(
                onDismissRequest = onDismiss,
                confirmButton = {
                    TextButton(onClick = onConfirm) {
                        Text("Activar",
                            color = MaterialTheme.colorScheme.primary)

                    }
                },
                dismissButton = {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar",
                            color = MaterialTheme.colorScheme.secondary)
                    }
                },

                title = { Text("¿Activar película?",
                    color = MaterialTheme.colorScheme.onSurface) },
                text = { Text("Esto activara la película y podra ser alquilada.",
                    color = MaterialTheme.colorScheme.onSurface) },
                icon = { Icon(Icons.Default.Warning, contentDescription = "", tint =
                MaterialTheme.colorScheme.onSurface) }
            )
        } else {
            AlertDialog(
                onDismissRequest = onDismiss,
                confirmButton = {
                    TextButton(onClick = onConfirm) {
                        Text("Aceptar")
                    }
                },
                title = { Text("No es posible activar la pelicula") },
                icon = { Icon(Icons.Default.Info, contentDescription = "", tint =
                MaterialTheme.colorScheme.onSurface) }
            )
        }
    }


}

@Composable
fun EditDialog(pelicula: ListProducts, onDismiss: () -> Unit, onConfirm: (Pelicula) -> Unit) {

    var titulo by remember { mutableStateOf(pelicula.titulo ?: "") }
    var genero by remember { mutableStateOf(pelicula.genero ?: "") }
    var anno by remember { mutableStateOf(pelicula.anio.toString() ?: "") }
    var director by remember { mutableStateOf(pelicula.director ?: "") }
    var sinopsis by remember { mutableStateOf(pelicula.sinopsis ?: "") }
    var image by remember { mutableStateOf(pelicula.imagen ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onConfirm(
                    Pelicula(
                        id_pelicula = pelicula.id_pelicula,
                        titulo = titulo,
                        genero = genero,
                        anio = anno.toInt(),
                        director = director,
                        sinopsis = sinopsis,
                        imagen = image,
                        clasificacion_edad = pelicula.clasificacion_edad
                    )
                )
            }) {
                Text("Guardar",
                    color = MaterialTheme.colorScheme.primary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar",
                    color = MaterialTheme.colorScheme.secondary)
            }
        },
        title = { Text("Editar película",
            color = MaterialTheme.colorScheme.onSurface) },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary)
                )
                OutlinedTextField(
                    value = genero,
                    onValueChange = { genero = it },
                    label = { Text("Genero") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary)
                )
                OutlinedTextField(
                    value = anno,
                    onValueChange = { anno = it },
                    label = { Text("Año") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary)
                )
                OutlinedTextField(
                    value = director,
                    onValueChange = { director = it },
                    label = { Text("Director") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary)
                )
                OutlinedTextField(
                    value = sinopsis,
                    onValueChange = { sinopsis = it },
                    label = { Text("Sinopsis") },
                    maxLines = 4,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary)
                )
                OutlinedTextField(
                    value = image,
                    onValueChange = { image = it },
                    label = { Text("URL imagen pelicula") },
                    maxLines = 4,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary)
                )
            }
        }
    )
}

@Composable
fun ChangeProduct(pelicula: ListProducts, onDismiss: () -> Unit, onConfirm: (Int) -> Unit) {
    var total by remember { mutableStateOf(pelicula.disponibles) }
    AlertDialog(onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onConfirm(total)
                onDismiss()
            }) {
                Text("Cambiar numero de productos", color = MaterialTheme.colorScheme.primary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = MaterialTheme.colorScheme.secondary)
            }
        }, title = { Text("Modificar cantidad total de productos", color = MaterialTheme.colorScheme.onSurface) },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Text("Cantidad total de copias en inventario: ${pelicula.total_productos}", color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.padding(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (total > pelicula.alquilados) {
                                total--
                            }
                        }
                    ) {
                        Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "", tint = MaterialTheme.colorScheme.onSurface)
                    }

                    Text(
                        text = total.toString(),
                        modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.onSurface
                    )

                    IconButton(
                        onClick = { total++ }
                    ) {
                        Icon(Icons.Default.KeyboardArrowRight, contentDescription = "", tint = MaterialTheme.colorScheme.onSurface)
                    }
                }

                Spacer(modifier = Modifier.padding(6.dp))

                Text(
                    "Productos disponibles: ${pelicula.disponibles}\n" +
                            "Productos alquilados: ${pelicula.alquilados}", color = MaterialTheme.colorScheme.onSurface
                )
            }


        })
}