package com.example.proyect_final.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyect_final.data.Pelicula
import com.example.proyect_final.data.RegisterValoracion
import com.example.proyect_final.data.RentMovie
import com.example.proyect_final.data.ResponseMessage
import com.example.proyect_final.data.Usuario
import com.example.proyect_final.data.Valoracion
import com.example.proyect_final.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class MovieViewModel : ViewModel(){

    private val _movie = MutableStateFlow<Pelicula?>(null)
    val movie = _movie.asStateFlow()

    private val _opinions = MutableStateFlow<List<Valoracion>>(emptyList())
    val opinions = _opinions.asStateFlow()

    private val _users = MutableStateFlow<Map<Int, Usuario>>(emptyMap())
    val users = _users.asStateFlow()

    private val _delopinion = MutableStateFlow<ResponseMessage?>(null)
    val delopinion = _delopinion.asStateFlow()

    private val _peliculas = MutableStateFlow<List<Pelicula>?>(emptyList())
    val peliculas = _peliculas.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _productRent = MutableStateFlow<Boolean?>(null)
    val productRent = _productRent.asStateFlow()

    init {
        getMovies()
    }

    fun loadMovie(id: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getProduct(id)
                if (response.isSuccessful) {
                    _movie.value = response.body()
                }
            } catch (e: Exception) {
                Log.e("MovieVM producto", "Error loading movie: ${e.message}")
            }
        }
    }

    fun loadOpinions(peliculaId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getOpinionId(peliculaId)
                if (response.isSuccessful) {
                    val list = response.body() ?: emptyList()
                    loadUsersForOpinions(list)
                    Log.d("Carga opiniones", list.toString())
                    _opinions.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                Log.e("MovieVM opinions", "Error loading opinions: ${e.message}")
            }
        }
    }

//    private fun loadUsersForOpinions() {
//        val currentUsers = _users.value.toMutableMap()
//        _opinions.value.forEach { valoracion ->
//            val userId = valoracion.usuario_id_usuario
//            if (!currentUsers.containsKey(userId)) {
//                RetrofitClient.apiService.getUserId(userId).enqueue(object : Callback<Usuario> {
//                    override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
//                        if (response.isSuccessful) {
//                            currentUsers[userId] = response.body()!!
//                            _users.value = currentUsers.toMap()
//                        }
//                    }
//
//                    override fun onFailure(call: Call<Usuario>, t: Throwable) {
//                        Log.e("MovieViewModel", "Error loading user: ${t.message}")
//                    }
//                })
//            }
//        }
//    }

    private fun loadUsersForOpinions(list: List<Valoracion>) {
        viewModelScope.launch {
            val userMap = _users.value.toMutableMap()
            for (v in list) {
                if (!userMap.containsKey(v.usuario_id_usuario)) {
                    try {
                        val res = RetrofitClient.apiService.getUserId(v.usuario_id_usuario)
                        if (res.isSuccessful && res.body() != null) {
                            userMap[v.usuario_id_usuario] = res.body()!!
                        }
                    } catch (_: Exception) {}
                }
            }
            _users.value = userMap.toMap()
        }
    }

//    fun postOpinion(opinion: RegisterValoracion) {
//        RetrofitClient.apiService.postRegisterOpinion(
//            opinion
//        ).enqueue(object : Callback<RegisterValoracion> {
//            override fun onResponse(
//                call: Call<RegisterValoracion>,
//                response: Response<RegisterValoracion>
//            ) {
//                if (response.isSuccessful) {
//                    loadOpinions(opinion.pelicula_id_pelicula)
//                    Log.d("registerValoracion", "${response.code()}")
//                } else {
//                    val errorBody = response.errorBody()?.string()
//                    Log.d("registerValoracion else", "${response.code()}, ${errorBody}")
//                }
//            }
//
//            override fun onFailure(call: Call<RegisterValoracion>, t: Throwable) {
//                Log.d("RegisterValoracion mal", t.message.toString())
//            }
//
//        })
//    }

    fun postOpinion(valoracion: RegisterValoracion) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.postRegisterOpinion(valoracion)
                if (response.isSuccessful) {
                    loadOpinions(valoracion.pelicula_id_pelicula)
                }
            } catch (e: Exception) {
                Log.e("MovieVM opinion post", "Error posting opinion: ${e.message}")
            }
        }
    }

//    RetrofitClient.apiService.delValoracion(op.id_valoracion)
//    .enqueue(object : Callback<ResponseMessage> {
//        override fun onResponse(
//            call: Call<ResponseMessage>,
//            response: Response<ResponseMessage>
//        ) {
//            if (response.isSuccessful) {
//                val body = response.body()
//                if (body != null) {
//                    Log.d(
//                        "Valoracion eliminada",
//                        body.toString()
//                    )
//                }
//            } else {
//                val body = response.body()
//                Log.d(
//                    "Valoracion no eliminada",
//                    "Error ${response.code()}: $body"
//                )
//            }
//        }
//
//        override fun onFailure(
//            call: Call<ResponseMessage>,
//            t: Throwable
//        ) {
//            Log.d(
//                "Valoracion",
//                "no se llego a conectar, ${
//                    t.stackTrace.get(0)
//                }"
//            )
//        }
//
//    })

    fun delOpinion(id_valoracion: Int){
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.delValoracion(id_valoracion)
                if (response.isSuccessful){
                    val body = response.body()
                    if (body != null) {
                        Log.d("DELETE", "Mensaje: ${body.message}")
                        _delopinion.value = body
                    } else {
                        Log.e("DELETE", "Response.body() es NULL")
                    }
                }
            }catch (e: Exception){
                Log.d("DelValoracion", "No se pudo borrar el comentario")
            }
        }
    }

    private fun getMovies() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getMovies()
                if (response.isSuccessful){
                    _peliculas.value = response.body()
                    _isLoading.value = false
                    Log.d("PELIS", "Películas recibidas: ${response.body()?.size}")
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
            _isLoading.value = false
        }
    }

    fun rentMovie(date: Date, id_movie: Int, id_user: Int){
        Log.d("rent ids", "user: $id_user, peli: $id_movie")
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.productRent(RentMovie(date, id_user, id_movie))
                if (response.isSuccessful){
                    Log.d("rent", response.errorBody().toString())
                    _productRent.value = response.isSuccessful
                }else{
                    Log.d("rent", response.errorBody().toString())
                    _productRent.value = false
                }
            }catch (e: Exception){

            }
        }
    }

}