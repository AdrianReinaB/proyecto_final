package com.example.proyect_final.data

data class Usuario(
    val id_usuario: Int = 0,
    val nombre: String = "",
    val apellido: String = "",
    val email: String = "",
    val rol: String = "",
    val credito: Int = 0,
    val password: String = "",
    val telefono: String = ""
)

data class UsuarioLogin(
    val email: String="",
    val password: String
)

data class UsuarioRegister(
    val nombre: String = "",
    val apellido: String = "",
    val email: String = "",
    val password: String = "",
    val telefono: Int = 0
)
