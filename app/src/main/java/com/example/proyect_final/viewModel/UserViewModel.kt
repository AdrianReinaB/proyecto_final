package com.example.proyect_final.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyect_final.data.Usuario
import com.example.proyect_final.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log
import com.example.proyect_final.data.RentMovieUser
import com.example.proyect_final.data.UsuarioRegister
import com.example.proyect_final.data.UsuarioRent
import com.example.proyect_final.data.UsuarioUpdate
import com.example.proyect_final.data.ValoracionUser

class UserViewModel : ViewModel() {

    var currentUser = mutableStateOf<Usuario?>(null)

    private val _users = MutableStateFlow<List<Usuario>?>(emptyList())
    val users = _users.asStateFlow()

    private val _userUpdated = MutableStateFlow<Boolean>(false)
    val userUpdated = _userUpdated.asStateFlow()

    private val _userActivated = MutableStateFlow<Boolean>(false)
    val userActivated = _userActivated.asStateFlow()

    private val _opinionForUser = MutableStateFlow<List<ValoracionUser>?>(emptyList())
    val opinionForUser = _opinionForUser.asStateFlow()

    private val _rentUser = MutableStateFlow<List<RentMovieUser>?>(emptyList())
    val rentUser = _rentUser.asStateFlow()

    fun setUser(user: Usuario) {
        currentUser.value = user
    }

    fun clearUser() {
        currentUser.value = null
    }

    fun reloadCurrentUser() {
        val id = currentUser.value?.id_usuario ?: return

        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getUserId(id)
                if (response.isSuccessful) {
                    currentUser.value = response.body()
                } else {
                    Log.e("UserVM", "Error recargando usuario")
                }
            } catch (e: Exception) {
                Log.e("UserVM", "Excepción: ${e.message}")
            }
        }
    }

    fun loadUsers() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getUsers()
                if (response.isSuccessful) {
                    _users.value = response.body()
                }
            } catch (e: Exception) {
                Log.e("UserVM", "Error cargando usuarios: ${e.message}")
            }
        }
    }

    fun updateUser(id: Int, user: UsuarioUpdate) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.updateUser(id, user)
                if (response.isSuccessful) {
                    _userUpdated.value = true
                    loadUsers()
                } else {
                    Log.e(
                        "UserVM update error",
                        "Error ${response.code()}: ${response.errorBody()?.string()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("UserVM", "Error al actualizar: ${e.message}")
            }
        }
    }

    fun toggleUserActive(id: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.actUser(id)
                if (response.isSuccessful) {
                    _userActivated.value = true
                    loadUsers()
                } else {
                    Log.e(
                        "UserVM activate error",
                        "Error ${response.code()}: ${response.errorBody()?.string()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("UserVM", "Error activando/desactivando: ${e.message}")
            }
        }
    }

    fun loadOpinionForUser(id: Int){
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getOpinionUser(id)
                if (response.isSuccessful){
                    _opinionForUser.value = response.body()
                }else{
                    Log.d("Opinion del usuario", "${response.errorBody()?.string()}")
                }
            }catch (e: Exception){
                    Log.d("Opinion del usuario", "Fallo al recibir las opiniones: ${e.message}")
            }
        }
    }

    fun loadRentUserMovie(id: Int){
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getRentUser(id)
                if (response.isSuccessful){
                    _rentUser.value = response.body()
                }else{
                    Log.d("Alquiler usuario", "${response.errorBody()?.string()}")
                }
            }catch (e: Exception){
                Log.d("Alquileres del usuario", "Fallo al recibir los alquileres: ${e.message}")
            }
        }
    }

}