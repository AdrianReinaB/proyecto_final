package com.example.proyect_final.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.proyect_final.R
import com.example.proyect_final.viewModel.RegisterMovieViewModel

@SuppressLint("Recycle")
@Composable
fun RegisterMovieScreen(registerMovieViewModel: RegisterMovieViewModel) {

    val scrollState = rememberScrollState()
    var registerMovieTittle by remember { mutableStateOf("") }
    var registerMovieSinopsis by remember { mutableStateOf("") }
    var registerMovieGenre by remember { mutableStateOf("") }
    var registerMovieDirector by remember { mutableStateOf("") }
    var registerMovieAge by remember { mutableStateOf("") }
    var registerMovieAnno by remember { mutableStateOf("") }
    var registerMovieUrlImage by remember { mutableStateOf("") }
    val registerMovieStatus by registerMovieViewModel.registerMovie.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(registerMovieStatus) {
        when(registerMovieStatus) {
            true -> Toast.makeText(context, "Registro de pelicula exitoso", Toast.LENGTH_SHORT).show()
            false -> Toast.makeText(context, "Error en el registro de pelicula", Toast.LENGTH_SHORT).show()
            null -> Unit
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(10.dp), contentAlignment = Alignment.Center
    ) {
        Column {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(
                    value = registerMovieTittle,
                    onValueChange = { registerMovieTittle = it },
                    label = { Text(stringResource(R.string.Titulo_pelicula),
                        color = MaterialTheme.colorScheme.primary) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
                OutlinedTextField(
                    value = registerMovieSinopsis,
                    onValueChange = { registerMovieSinopsis = it },
                    label = { Text(stringResource(R.string.Sinopsis_pelicula),
                        color = MaterialTheme.colorScheme.primary) },
                    maxLines = 4,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
                OutlinedTextField(
                    value = registerMovieDirector,
                    onValueChange = { registerMovieDirector = it },
                    label = { Text(stringResource(R.string.Director_pelicula),
                        color = MaterialTheme.colorScheme.primary) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
                OutlinedTextField(
                    value = registerMovieGenre,
                    onValueChange = { registerMovieGenre = it },
                    label = { Text(stringResource(R.string.Genero_pelicula),
                        color = MaterialTheme.colorScheme.primary) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
                OutlinedTextField(
                    value = registerMovieAnno,
                    onValueChange = { registerMovieAnno = it },
                    label = { Text(stringResource(R.string.Anio_pelicula),
                        color = MaterialTheme.colorScheme.primary) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
                OutlinedTextField(
                    value = registerMovieAge,
                    onValueChange = { registerMovieAge = it },
                    label = { Text(stringResource(R.string.Edad_pelicula),
                        color = MaterialTheme.colorScheme.primary) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
                OutlinedTextField(
                    value = registerMovieUrlImage,
                    onValueChange = { registerMovieUrlImage = it },
                    label = { Text("URL imagen pelicula",
                        color = MaterialTheme.colorScheme.primary) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
            }
            Button(
                onClick = {
                    if (registerMovieTittle.isEmpty()||registerMovieDirector.isEmpty()||registerMovieAge.isEmpty()||registerMovieAnno.isEmpty()||registerMovieGenre.isEmpty()){
                        Toast.makeText(context, "Debes rellenar todos los campos requeridos🚫", Toast.LENGTH_SHORT).show()
                    } else {
                        registerMovieViewModel.registerMovie(
                            registerMovieTittle,
                            registerMovieSinopsis,
                            registerMovieAge,
                            registerMovieGenre,
                            registerMovieDirector,
                            registerMovieAnno,
                            registerMovieUrlImage
                        )
                    }

                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Text(stringResource(R.string.Registrar_pelicula),
                    color = MaterialTheme.colorScheme.surface)
            }
        }

    }
}