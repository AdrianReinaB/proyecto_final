package com.example.proyect_final.data


data class Pelicula(
    val id_pelicula: Int = 0,
    val titulo: String = "",
    val genero: String = "",
    val año: Int = 0,
    val director: String = "",
    val clasificacion_edad: String = "",
    val sinopsis: String = "",
    val imagen: String = "",
    val numero_disponibles: Int = 0,
)

data class RegisterPelicula(
    val titulo: String = "",
    val genero: String = "",
    val año: Int = 0,
    val director: String = "",
    val clasificacion_edad: String = "",
    val sinopsis: String = "",
    val imagen: String = "",
)
