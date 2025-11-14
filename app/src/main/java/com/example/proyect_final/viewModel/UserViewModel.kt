package com.example.proyect_final.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.proyect_final.data.Usuario

class UserViewModel : ViewModel() {

    var currentUser = mutableStateOf<Usuario?>(null)

    fun setUser(user: Usuario) {
        currentUser.value = user
    }

    fun clearUser() {
        currentUser.value = null
    }
}