package com.example.proyect_final.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyect_final.data.UsuarioRent
import com.example.proyect_final.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserRentViewModel: ViewModel() {

    private val _rents = MutableStateFlow<List<UsuarioRent>>(emptyList())
    val rents = _rents.asStateFlow()

    fun getRents(){
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getRents()
                if (response.isSuccessful){

                }else{

                }
            }catch (e: Exception){

            }
        }
    }
}