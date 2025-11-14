package com.example.proyect_final.navegation

import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object Register

@Serializable
object Movies

@Serializable
data class DetailMovie(val id: Int)

@Serializable
object RegisterMovie

@Serializable
object Profile

@Serializable
object RentMovies