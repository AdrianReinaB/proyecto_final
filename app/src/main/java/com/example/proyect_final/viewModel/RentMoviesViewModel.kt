package com.example.proyect_final.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyect_final.data.UsuarioRent
import com.example.proyect_final.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RentMoviesViewModel: ViewModel() {

    private val _returnMovie = MutableStateFlow<Boolean?>(null)
    val returnMovie = _returnMovie.asStateFlow()

    private val _rents = MutableStateFlow<List<UsuarioRent>?>(emptyList())
    val rents = _rents.asStateFlow()

    fun returnMovie(id_movie: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.returnProduct(id_movie)
                if (response.isSuccessful){
                    _returnMovie.value = response.isSuccessful
                    getRents()
                }else{
                    _returnMovie.value = false
                }
            }catch (e: Exception){
                Log.d("Return movie", "dont, ${e.message}")
            }
        }
    }

    fun getRents(){
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getRents()
                if (response.isSuccessful){
                    _rents.value = response.body()
                }else{

                }
            }catch (e: Exception){

            }
        }
    }
}