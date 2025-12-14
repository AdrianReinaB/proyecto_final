package com.example.proyect_final.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyect_final.data.RegisterPelicula
import com.example.proyect_final.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterMovieViewModel: ViewModel() {

    private val _registerMovie = MutableStateFlow<Boolean?>(null)
    val registerMovie = _registerMovie.asStateFlow()

    fun registerMovie(
        tittle: String,
        sinopsis: String,
        movieAge: String,
        genrer: String,
        director: String,
        anio: String,
        image: String
    ) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.postRegisterMovie(
                    RegisterPelicula(
                        tittle,
                        sinopsis,
                        movieAge.toInt(),
                        genrer,
                        director,
                        anio,
                        image
                    )
                )
                if (response.isSuccessful) {
                    _registerMovie.value = response.isSuccessful
                } else {
                    _registerMovie.value = false
                }
            } catch (e: Exception) {
                Log.d("RegisterMovie", "dont, ${e.message}")
            }
        }
    }
}