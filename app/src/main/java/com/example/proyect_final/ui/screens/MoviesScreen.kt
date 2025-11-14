package com.example.proyect_final.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults.TonalElevation
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.proyect_final.data.Pelicula
import com.example.proyect_final.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun MoviesScreen(
    navigateToMovie: (Int) -> Unit,
    isSearchActive: Boolean,
    onCloseSearch: () -> Unit
) {

    var peliculas by remember { mutableStateOf<List<Pelicula>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var query by rememberSaveable { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf<String?>("") }

    LaunchedEffect(Unit) {
        RetrofitClient.apiService.getMovies().enqueue(object : Callback<List<Pelicula>> {
            override fun onResponse(
                call: Call<List<Pelicula>>,
                response: Response<List<Pelicula>>
            ) {
                if (response.isSuccessful) {
                    peliculas = response.body() ?: emptyList()
                } else {
                    Log.d("Peliculas", "Error ${response.code()}")
                }
                isLoading = false
            }

            override fun onFailure(call: Call<List<Pelicula>>, t: Throwable) {
                Log.e("Peliculas", "Error: ${t.message}")
                isLoading = false
            }
        })
    }

    val peliculasFiltradas =
        if (selectedGenre != "") {
            peliculas.filter { it.genero.equals(selectedGenre, ignoreCase = true) }
        } else {
            peliculas
        }


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (isLoading) {
            Text(text = "Cargando...")
            Log.d("Espera", "cargando")
        } else {
            LazyVerticalGrid(columns = GridCells.Fixed(2), contentPadding = PaddingValues(10.dp)) {
                items(peliculasFiltradas) { pelicula ->
                    MovieCard(pelicula, { navigateToMovie(pelicula.id_pelicula) })
                }
//            item {
//                ProductCard("nombre peli", "https://th.bing.com/th/id/R.6bfebb09fd003f700f38c1f8da8155f0?rik=QFDbzC9mAqai3g&pid=ImgRaw&r=0")
//            }
//            item {
//                ProductCard("peli", "https://i.pinimg.com/originals/82/d2/5b/82d25b943e9c7a1f9e801f498461d4f6.jpg")
//            }
//            item {
//                ProductCard("peli", "https://www.laguiadelvaron.com/wp-content/uploads/2019/07/portadas-pel%C3%ADculas-iguales-www.laguiadelvaron-15.jpg")
//            }
//            item {
//                ProductCard("nombre peli", "https://th.bing.com/th/id/R.6bfebb09fd003f700f38c1f8da8155f0?rik=QFDbzC9mAqai3g&pid=ImgRaw&r=0")
//            }
//            item {
//                ProductCard("peli", "https://i.pinimg.com/originals/82/d2/5b/82d25b943e9c7a1f9e801f498461d4f6.jpg")
//            }
//            item {
//                ProductCard("peli", "https://www.laguiadelvaron.com/wp-content/uploads/2019/07/portadas-pel%C3%ADculas-iguales-www.laguiadelvaron-15.jpg")
//            }
//            item {
//                ProductCard("nombre peli", "https://th.bing.com/th/id/R.6bfebb09fd003f700f38c1f8da8155f0?rik=QFDbzC9mAqai3g&pid=ImgRaw&r=0")
//            }
//            item {
//                ProductCard("peli", "https://i.pinimg.com/originals/82/d2/5b/82d25b943e9c7a1f9e801f498461d4f6.jpg")
//            }
//            item {
//                ProductCard("peli", "https://www.laguiadelvaron.com/wp-content/uploads/2019/07/portadas-pel%C3%ADculas-iguales-www.laguiadelvaron-15.jpg")
//            }
//            item {
//                ProductCard("nombre peli", "https://th.bing.com/th/id/R.6bfebb09fd003f700f38c1f8da8155f0?rik=QFDbzC9mAqai3g&pid=ImgRaw&r=0")
//            }
//            item {
//                ProductCard("peli", "https://i.pinimg.com/originals/82/d2/5b/82d25b943e9c7a1f9e801f498461d4f6.jpg")
//            }
//            item {
//                ProductCard("peli", "https://www.laguiadelvaron.com/wp-content/uploads/2019/07/portadas-pel%C3%ADculas-iguales-www.laguiadelvaron-15.jpg")
//            }
//            item {
//                ProductCard("nombre peli", "https://th.bing.com/th/id/R.6bfebb09fd003f700f38c1f8da8155f0?rik=QFDbzC9mAqai3g&pid=ImgRaw&r=0")
//            }
//            item {
//                ProductCard("peli", "https://i.pinimg.com/originals/82/d2/5b/82d25b943e9c7a1f9e801f498461d4f6.jpg")
//            }
//            item {
//                ProductCard("peli", "https://www.laguiadelvaron.com/wp-content/uploads/2019/07/portadas-pel%C3%ADculas-iguales-www.laguiadelvaron-15.jpg")
//            }
            }
        }
        MoviesContent(
            isSearchActive = isSearchActive,
            query = query,
            onQueryChange = { query = it },
            onCloseSearch = onCloseSearch,
            movies = peliculas,
            onGenreSelected = { selectedGenre = it },
            navigateToMovie = navigateToMovie
        )
    }
}

@Composable
fun MovieCard(pelis: Pelicula, onClickMovie: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(3.dp))
            .padding(5.dp)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .height(250.dp)
            .clickable {
                onClickMovie()
            }, contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(10.dp)
        ) {
            AsyncImage(
                model = pelis.imagen,
                contentDescription = "",
                modifier = Modifier
                    .size(200.dp)
                    .padding(5.dp)
            )
            Text(pelis.titulo, modifier = Modifier.padding(5.dp), fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesContent(
    isSearchActive: Boolean,
    query: String,
    onQueryChange: (String) -> Unit,
    onCloseSearch: () -> Unit,
    movies: List<Pelicula>,
    onGenreSelected: (String) -> Unit,
    navigateToMovie: (Int) -> Unit
) {
    var searchMovies by remember { mutableStateOf(true) }
    val filteredMovies = movies.filter {
        it.titulo.contains(query, ignoreCase = true)
    }
    val filteredMoviesGenrer = movies.filter {
        it.genero.contains(it.genero)
    }.distinctBy { it.genero }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isSearchActive) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = {
                        searchMovies = true
                    }) { Text("Busqueda peliculas") }
                    TextButton(onClick = {
                        searchMovies = false
                    }) { Text("Busqueda por genero") }
                }
                if (searchMovies) {
                    SearchBar(
                        query = query,
                        onQueryChange = onQueryChange,
                        onSearch = { },
                        active = true,
                        onActiveChange = { active ->
                            if (!active) onCloseSearch()
                        },
                        placeholder = { Text("Buscar película...") },
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        tonalElevation = TonalElevation,
                    ) {
                        filteredMovies.take(10).forEach { peli ->
                            Text(
                                text = peli.titulo,
                                modifier = Modifier
                                    .padding(12.dp)
                                    .clickable {
                                        navigateToMovie(peli.id_pelicula)
                                        onCloseSearch()
                                    }
                            )
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            item {
                                Text("Generos", modifier = Modifier.padding(10.dp))
                                HorizontalDivider()
                            }
                            item {
                                Text("Sin genero", modifier = Modifier
                                    .padding(10.dp)
                                    .clickable {
                                        onGenreSelected("")
                                        onCloseSearch()
                                        searchMovies = true
                                    })
                            }
                            items(filteredMoviesGenrer) {
                                Text(it.genero, modifier = Modifier
                                    .padding(10.dp)
                                    .clickable {
                                        onGenreSelected(it.genero)
                                        onCloseSearch()
                                        searchMovies = true
                                    })
                            }
                        }
                    }
                }
            }
        }
    }
}
