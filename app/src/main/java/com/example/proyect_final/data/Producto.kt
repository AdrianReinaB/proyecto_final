package com.example.proyect_final.data


data class Producto(
    val id_producto: Int = 0,
    val fecha_inicio: String = "",
    val fecha_fin: String = "",
    val estado: String = "",
    val usuario_id_usuario: Int = 0,
    val pelicula_id_pelicula: Int = 0,
)

data class RentMovie(
    val fecha_inicio: String = "",
    val fecha_fin: String = "",
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
)
