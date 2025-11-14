package com.example.proyect_final.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.proyect_final.data.RentMovieUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun RentMoviesUser(userViewModel: UserViewModel){
    var rents by remember { mutableStateOf<List<RentMovieUser>>(emptyList()) }
    val user = userViewModel.currentUser.value

    LaunchedEffect(user) {
        user?.let {
            RetrofitClient.apiService.getRentUser(it.id_usuario)
                .enqueue(object : Callback<List<RentMovieUser>> {
                    override fun onResponse(
                        call: Call<List<RentMovieUser>>, response: Response<List<RentMovieUser>>
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

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        if (rents.isEmpty()) {
            Text("Sin alquileres")
        }else{
            LazyVerticalGrid(columns = GridCells.Adaptive(150.dp), contentPadding = PaddingValues(12.dp)) {
                items(rents) { movie ->
                    RentMovieCard(movie)
                }
            }
        }
    }
}

@Composable
fun RentMovieCard(pelis: RentMovieUser) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(3.dp))
            .padding(5.dp)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(10.dp)
        ) {
            AsyncImage(
                model = pelis.titulo,
                contentDescription = "",
                modifier = Modifier
                    .size(200.dp)
                    .padding(5.dp)
            )
            Text(pelis.titulo, modifier = Modifier.padding(5.dp), fontWeight = FontWeight.Bold)
            Text("Vencimiento:\n${pelis.fecha_fin}", modifier = Modifier.padding(5.dp), fontWeight = FontWeight.Bold)
        }
    }
}