package com.example.proyect_final.data

import java.time.LocalDate
import java.util.Date


data class Producto(
    val id_producto: Int = 0,
    val fecha_inicio: Date,
    val fecha_fin: Date,
    val estado: String = "",
    val usuario_id_usuario: Int = 0,
    val pelicula_id_pelicula: Int = 0,
)

data class RentMovie(
    val fecha_fin: Date,
    val usuario_id_usuario: Int = 0,
    val pelicula_id_pelicula: Int = 0,
)

data class RentMovieUser(
    val id_producto: Int = 0,
    val fecha_inicio: String = "",
    val fecha_fin: String = "",
    val estado: String = "",
    val usuario_id_usuario: Int = 0,
    val pelicula_id_pelicula: Int = 0,
    val titulo: String = "",
    val imagen: String = "",
)

data class ListProducts(
    val total_productos: Int = 0,
    val disponibles: Int = 0,
    val id_pelicula: Int = 0,
    val titulo: String = "",
    val genero: String = "",
    val anio: Int = 0,
    val director: String = "",
    val clasificacion_edad: String = "",
    val sinopsis: String = "",
    val imagen: String = "",
    val disponibilidad: String = "",
    val activa: Int = 0,
    val alquilados: Int = 0,
)

data class ChangePro(
    val total: Int = 0,
)
