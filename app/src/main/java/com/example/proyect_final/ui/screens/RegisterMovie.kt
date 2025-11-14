package com.example.proyect_final.ui.screens

import android.util.Log
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.proyect_final.data.RegisterPelicula
import com.example.proyect_final.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun RegisterMovieScreen() {

    val scrollState = rememberScrollState()
    var registerMovieTittle by remember { mutableStateOf("") }
    var registerMovieSinopsis by remember { mutableStateOf("") }
    var registerMovieGenre by remember { mutableStateOf("") }
    var registerMovieDirector by remember { mutableStateOf("") }
    var registerMovieAge by remember { mutableStateOf("") }
    var registerMovieAnno by remember { mutableStateOf("") }
    var registerMovieUrlImage by remember { mutableStateOf("") }

//    var photo by remember { mutableStateOf<Uri?>(null) }
//    val pickMedia =
//        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia(), onResult = {
//            photo = it
//        })
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(10.dp), contentAlignment = Alignment.Center
    ) {
        Column {
            Column {
//                if (photo != null) {
//                    Log.d("foto", photo.toString())
//                    AsyncImage(
//                        model = photo,
//                        contentDescription = "",
//                        modifier = Modifier
//                            .size(200.dp)
//                            .fillMaxWidth()
//                    )
//                } else {
//                    IconButton(onClick = {
//                        pickMedia.launch(
//                            PickVisualMediaRequest(
//                                ActivityResultContracts.PickVisualMedia.ImageOnly
//                            )
//                        )
//                    }, modifier = Modifier.fillMaxWidth()) {
//                        Icon(Icons.Default.AccountCircle, contentDescription = "")
//                    }
//                }
                OutlinedTextField(
                    value = registerMovieTittle,
                    onValueChange = { registerMovieTittle = it },
                    label = { Text("Titulo pelicula") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
                OutlinedTextField(
                    value = registerMovieSinopsis,
                    onValueChange = { registerMovieSinopsis = it },
                    label = { Text("Sinopsis pelicula") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
                OutlinedTextField(
                    value = registerMovieDirector,
                    onValueChange = { registerMovieDirector = it },
                    label = { Text("Director pelicula") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
                OutlinedTextField(
                    value = registerMovieGenre,
                    onValueChange = { registerMovieGenre = it },
                    label = { Text("genero pelicula") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
                OutlinedTextField(
                    value = registerMovieAnno,
                    onValueChange = { registerMovieAnno = it },
                    label = { Text("Año pelicula") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
                OutlinedTextField(
                    value = registerMovieAge,
                    onValueChange = { registerMovieAge = it },
                    label = { Text("Edad recomendada pelicula") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
                OutlinedTextField(
                    value = registerMovieUrlImage,
                    onValueChange = { registerMovieUrlImage = it },
                    label = { Text("Imagen pelicula") },
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
                    }else{
                        RetrofitClient.apiService.postRegisterMovie(
                            RegisterPelicula(
                                titulo = registerMovieTittle,
                                sinopsis = registerMovieSinopsis,
                                clasificacion_edad = registerMovieAge,
                                genero = registerMovieGenre,
                                director = registerMovieDirector,
                                año = registerMovieAnno.toInt(),
                                imagen = registerMovieUrlImage
                            )
                        ).enqueue(object : Callback<RegisterPelicula> {
                            override fun onResponse(
                                call: Call<RegisterPelicula>,
                                response: Response<RegisterPelicula>
                            ) {
                                if (response.isSuccessful) {
                                    Log.d(
                                        "Registro existoso movie",
                                        "Registro de pelicula satisfactorio"
                                    )
                                } else {
                                    val errorBody = response.errorBody()?.string()
                                    Log.d(
                                        "Registro movie",
                                        "Registro no hecho: ${response.code()}, $errorBody"
                                    )
                                }
                            }

                            override fun onFailure(call: Call<RegisterPelicula>, t: Throwable) {
                                Log.d("Registro fail movie", t.message.toString())
                            }
                        })
                    }

                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Text("Registrar pelicula")
            }
        }

    }
}