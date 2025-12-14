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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.proyect_final.R
import com.example.proyect_final.data.Pelicula
import com.example.proyect_final.viewModel.MovieViewModel

@Composable
fun MoviesScreen(
    navigateToMovie: (Int) -> Unit,
    isSearchActive: Boolean,
    onCloseSearch: () -> Unit,
    movieView: MovieViewModel
) {

    val peliculas by movieView.peliculas.collectAsState()
    val isLoading by movieView.isLoading.collectAsState()
    var query by rememberSaveable { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf<String?>("") }

    Log.d("peliculas", peliculas.toString())

    val filterMovies = remember(peliculas, selectedGenre) {
        if (!selectedGenre.isNullOrEmpty()) {
            peliculas?.filter { it.genero.equals(selectedGenre, true) } ?: emptyList()
        } else{
            peliculas ?: emptyList()
        }
    }


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (isLoading) {
            Text(text = stringResource(R.string.Cargando), color = MaterialTheme.colorScheme.onSurface)
        } else {
            LazyVerticalGrid(columns = GridCells.Fixed(2), contentPadding = PaddingValues(10.dp)) {
                items(filterMovies) { pelicula ->
                    MovieCard(pelicula, { navigateToMovie(pelicula.id_pelicula) })
                }
            }
        }
        peliculas?.let {
            MoviesFilter(
                isSearchActive = isSearchActive,
                query = query,
                onQueryChange = { query = it },
                onCloseSearch = onCloseSearch,
                movies = it,
                onGenreSelected = { selectedGenre = it },
                navigateToMovie = navigateToMovie
            )
        }
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
                modifier = Modifier.size(180.dp)
            )
            Text(pelis.titulo, modifier = Modifier.padding(5.dp), overflow = TextOverflow.Ellipsis , fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesFilter(
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
    val filteredMoviesGenrer = remember(movies) {
        movies.distinctBy { it.genero }
    }

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
                    }) { Text(stringResource(R.string.Busqueda_peliculas), color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold) }
                    TextButton(onClick = {
                        searchMovies = false
                    }) { Text(stringResource(R.string.Busqueda_genero), color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold) }
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
                        placeholder = { Text(stringResource(R.string.Buscar_pelicula), color = MaterialTheme.colorScheme.onSurface) },
                        modifier = Modifier
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
                                    }, color = MaterialTheme.colorScheme.onSurface
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
                                Text(stringResource(R.string.Generos), modifier = Modifier.padding(10.dp), color = MaterialTheme.colorScheme.onSurface)
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline)

                            }
                            item {
                                Text(
                                    stringResource(R.string.Sin_genero), modifier = Modifier
                                        .padding(10.dp)
                                        .clickable {
                                            onGenreSelected("")
                                            onCloseSearch()
                                            searchMovies = true
                                        }, color = MaterialTheme.colorScheme.onSurface)
                            }
                            items(filteredMoviesGenrer) {
                                Text(it.genero, modifier = Modifier
                                    .padding(10.dp)
                                    .clickable {
                                        onGenreSelected(it.genero)
                                        onCloseSearch()
                                        searchMovies = true
                                    }, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }
                }
            }
        }
    }
}
