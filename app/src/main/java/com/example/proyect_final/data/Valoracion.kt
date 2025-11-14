package com.example.proyect_final.data

data class Valoracion(
    val id_valoracion: Int = 0,
    val puntuacion: Int = 0,
    val fecha: String = "",
    val comentario: String = "",
    val usuario_id_usuario: Int = 0,
    val pelicula_id_pelicula: Int = 0,
)

data class RegisterValoracion(
    val puntuacion: Int = 0,
    val comentario: String = "",
    val usuario_id_usuario: Int = 0,
    val pelicula_id_pelicula: Int = 0,
)

data class ValoracionUser(
    val id_valoracion: Int = 0,
    val puntuacion: Int = 0,
    val fecha: String = "",
    val comentario: String = "",
    val usuario_id_usuario: Int = 0,
    val pelicula_id_pelicula: Int = 0,
    val titulo: String = "",
)
