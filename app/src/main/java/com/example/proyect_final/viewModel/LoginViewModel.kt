package com.example.proyect_final.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyect_final.data.Usuario
import com.example.proyect_final.data.UsuarioLogin
import com.example.proyect_final.data.UsuarioRegister
import com.example.proyect_final.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    private val _user = MutableStateFlow<Usuario?>(null)
    val user = _user.asStateFlow()

    private val _userSuccess = MutableStateFlow<Boolean?>(null)
    val userSuccess = _userSuccess.asStateFlow()

    private val _registerSuccess = MutableStateFlow<Boolean?>(null)
    val registerSuccess = _registerSuccess.asStateFlow()

    fun loginUser(mail: String, pass: String){
        Log.d("login", "1entra")
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.postData(UsuarioLogin(mail, pass))
                if (response.isSuccessful){
                    val usuario = response.body()
                    _user.value = usuario
                }else{
                    _userSuccess.value = false
                }
            }catch (e: Exception){
            }
        }
    }

    fun registerUser(name: String, lastName: String, dni: String, mail: String, pass: String, phone: String){
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.postRegister(UsuarioRegister(name, lastName,dni, mail, pass, phone.toInt()))
                if (response.isSuccessful){
                    _registerSuccess.value = response.isSuccessful
                }else{
                    _registerSuccess.value = false
                }
            }catch (e: Exception){
            }
        }
    }
}