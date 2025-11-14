package com.example.proyect_final.network

import com.example.proyect_final.data.Pelicula
import com.example.proyect_final.data.Producto
import com.example.proyect_final.data.RegisterPelicula
import com.example.proyect_final.data.RegisterValoracion
import com.example.proyect_final.data.RentMovie
import com.example.proyect_final.data.RentMovieUser
import com.example.proyect_final.data.Usuario
import com.example.proyect_final.data.UsuarioLogin
import com.example.proyect_final.data.UsuarioRegister
import com.example.proyect_final.data.Valoracion
import com.example.proyect_final.data.ValoracionUser
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    //Pelicula
    @GET("peliculas")
    fun getMovies(): Call<List<Pelicula>>

    @GET("pelicula/{id_pelicula}")
    suspend fun getMovie(@Path("id_pelicula") id: Int): Response<Pelicula>

    @POST("registerMovie")
    fun postRegisterMovie(@Body movie: RegisterPelicula): Call<RegisterPelicula>

    //Valoracion
    @GET("valoracion/{id_pelicula}")
    suspend fun getOpinionId(@Path("id_pelicula") id: Int): Response<List<Valoracion>>

    @GET("valoracion/usuario/{id_user}")
    fun getOpinionUser(@Path("id_user") id: Int): Call<List<ValoracionUser>>

    @POST("registerOpinion")
    suspend fun postRegisterOpinion(@Body opinion: RegisterValoracion): Response<RegisterValoracion>

    //Usuario
    @GET("usuario/{id_usuario}")
   suspend fun getUserId(@Path("id_usuario") id: Int): Response<Usuario>

    @POST("login")
    fun postData(@Body usuario: UsuarioLogin): Call<Usuario>

    @POST("register")
    fun postRegister(@Body usuario: UsuarioRegister): Call<Usuario>

    @PUT("usuario/{id_usuario}")
    fun updateUser(@Path("id_usuario") id: Int, @Body user: UsuarioRegister): Call<Usuario>

    //Producto
    @POST("productRent")
    fun productRent(@Body product: RentMovie): Call<Producto>

    @GET("producto/usuario/{id_usuario}")
    fun getRentUser(@Path("id_usuario") id: Int): Call<List<RentMovieUser>>


}