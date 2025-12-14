package com.example.proyect_final.data

import java.time.LocalDate
import java.util.Date

data class Usuario(
    val id_usuario: Int = 0,
    val nombre: String = "",
    val apellido: String = "",
    val dni: String = "",
    val email: String = "",
    val rol: String = "",
    val credito: String = "",
    val password: String = "",
    val telefono: String = "",
    val activa: Int = 0
)

data class UsuarioLogin(
    val email: String="",
    val password: String
)

data class UsuarioRegister(
    val nombre: String = "",
    val apellido: String = "",
    val dni: String = "",
    val email: String = "",
    val password: String = "",
    val telefono: Int = 0,
)

data class UsuarioUpdate(
    val nombre: String = "",
    val apellido: String = "",
    val dni: String = "",
    val email: String = "",
    val password: String = "",
    val credito: Int = 0,
    val rol: String = "",
    val telefono: Int = 0,
)

data class UsuarioRent(
    val id_usuario: Int = 0,
    val nombre: String = "",
    val dni: String = "",
    val titulo: String = "",
    val estado_pr: String = "",
    val estado_a: String = "",
    val fecha_fin: String = "",
    val fecha_inicio: String = "",
    val id_producto: Int = 0,
    val id_alquiler: Int =0,
)
