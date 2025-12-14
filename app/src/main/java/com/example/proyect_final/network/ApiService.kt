package com.example.proyect_final.network

import com.example.proyect_final.data.ChangePro
import com.example.proyect_final.data.ListProducts
import com.example.proyect_final.data.Pelicula
import com.example.proyect_final.data.Producto
import com.example.proyect_final.data.RegisterPelicula
import com.example.proyect_final.data.RegisterValoracion
import com.example.proyect_final.data.RentMovie
import com.example.proyect_final.data.RentMovieUser
import com.example.proyect_final.data.ResponseMessage
import com.example.proyect_final.data.Usuario
import com.example.proyect_final.data.UsuarioLogin
import com.example.proyect_final.data.UsuarioRegister
import com.example.proyect_final.data.UsuarioRent
import com.example.proyect_final.data.UsuarioUpdate
import com.example.proyect_final.data.Valoracion
import com.example.proyect_final.data.ValoracionUser
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    //Pelicula
    @GET("peliculas")
    suspend fun getMovies(): Response<List<Pelicula>>

    @GET("pelicula/{id_pelicula}")
    suspend fun getMovie(@Path("id_pelicula") id: Int): Response<Pelicula>

    @POST("registerMovie")
    suspend fun postRegisterMovie(@Body movie: RegisterPelicula): Response<RegisterPelicula>

    @PUT("movie/{id_pelicula}")
    suspend fun editMovie(@Path("id_pelicula") id: Int, @Body movie: Pelicula): Response<Pelicula>

    //Valoracion
    @GET("valoracion/{id_pelicula}")
    suspend fun getOpinionId(@Path("id_pelicula") id: Int): Response<List<Valoracion>>

    @GET("valoracion/usuario/{id_user}")
    suspend fun getOpinionUser(@Path("id_user") id: Int): Response<List<ValoracionUser>>

    @POST("registerOpinion")
    suspend fun postRegisterOpinion(@Body opinion: RegisterValoracion): Response<RegisterValoracion>

    @DELETE("delvaloracion/{id_valoracion}")
    suspend fun delValoracion(@Path("id_valoracion") id: Int): Response<ResponseMessage>

    //Usuario
    @GET("usuarios")
    suspend fun getUsers(): Response<List<Usuario>>

    @GET("usuario/{id_usuario}")
    suspend fun getUserId(@Path("id_usuario") id: Int): Response<Usuario>

    @POST("login")
    suspend fun postData(@Body usuario: UsuarioLogin): Response<Usuario>

    @POST("register")
    suspend fun postRegister(@Body usuario: UsuarioRegister): Response<Usuario>

    @PUT("usuario/{id_usuario}")
    suspend fun updateUser(@Path("id_usuario") id: Int, @Body user: UsuarioUpdate): Response<Usuario>

    @PUT("/usuario/disabled/{id_usuario}")
    suspend fun actUser(@Path("id_usuario") id: Int): Response<ResponseMessage>

    //Producto
    @POST("productRent")
    suspend fun productRent(@Body product: RentMovie): Response<Producto>

    @GET("producto/{id_peli}")
    suspend fun getProduct(@Path("id_peli") id: Int): Response<Pelicula>

    @GET("listproducto")
    suspend fun getListProduct(): Response<List<ListProducts>>

    @PUT("producto/updateTotal/{id_pelicula}")
    suspend fun updateTotalProductos(@Path("id_pelicula") id: Int, @Body body: ChangePro): Response<ResponseMessage>

    @DELETE("delproducto/{id_pelicula}")
    suspend fun delProducto(@Path("id_pelicula") id: Int): Response<ResponseMessage>

    @PUT("actPelicula/{id_pelicula}")
    suspend fun actProducto(@Path("id_pelicula") id: Int): Response<ResponseMessage>

    //Alquiler
    @GET("alquiler/usuario/{id_usuario}")
    suspend fun getRentUser(@Path("id_usuario") id: Int): Response<List<RentMovieUser>>

    @POST("devolverproducto/{id_producto}")
    suspend fun returnProduct(@Path("id_producto") id: Int): Response<ResponseMessage>

    @GET("alquileres")
    suspend fun getRents(): Response<List<UsuarioRent>>



}