package com.example.proyect_final.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyect_final.data.ChangePro
import com.example.proyect_final.data.ListProducts
import com.example.proyect_final.data.Pelicula
import com.example.proyect_final.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListMoviesViewModel: ViewModel() {

    private val _listProduct = MutableStateFlow<List<ListProducts>?>(emptyList())
    val listProduct = _listProduct.asStateFlow()


    fun listProduct(){
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getListProduct()
                if (response.isSuccessful){
                    _listProduct.value = response.body()
                }else{

                }
            }catch (e: Exception){

            }
        }
    }

    fun delProduct(id_movie: Int){
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.delProducto(id_movie)
                if (response.isSuccessful){
                    listProduct()
                }else{

                }
            }catch (e: Exception){

            }
        }
    }

    fun actProduct(id_movie: Int){
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.actProducto(id_movie)
                if (response.isSuccessful){
                    listProduct()
                }else{
                }
            }catch (e: Exception){

            }
        }
    }

    fun editMovie(id_movie: Int, editMovie: Pelicula){
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.editMovie(id_movie, editMovie)
                if (response.isSuccessful){
                    listProduct()
                }
            }catch (e: Exception){

            }
        }
    }

    fun updateTotalProduct(id_movie: Int, total: Int){
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.updateTotalProductos(id_movie, ChangePro(total))
                if (response.isSuccessful){
                    listProduct()
                }else{

                }
            }catch (e: Exception){

            }
        }
    }
}