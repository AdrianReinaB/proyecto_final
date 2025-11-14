package com.example.proyect_final.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyect_final.data.Pelicula
import com.example.proyect_final.data.Usuario
import com.example.proyect_final.data.Valoracion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel(){

    private val _movie = MutableStateFlow<Pelicula?>(null)
    val movie = _movie.asStateFlow()

    private val _opinions = MutableStateFlow<List<Valoracion>>(emptyList())
    val opinions = _opinions.asStateFlow()

    private val _users = MutableStateFlow<Map<Int, Usuario>>(emptyMap())
    val users = _users.asStateFlow()

//    fun loadMovie(id: Int){
//        RetrofitClient.apiService.getMovie(id).enqueue(object : Callback<Pelicula> {
//            override fun onResponse(
//                call: Call<Pelicula>,
//                response: Response<Pelicula>
//            ) {
//                if (response.isSuccessful) {
//                    _movie.value = response.body()
//                    Log.d("pelicula", movie.toString())
//                } else {
//                    Log.d("Pelicula", "Error ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<Pelicula>, t: Throwable) {
//                Log.e("Pelicula", "Error: ${t.message}")
//            }
//        })
//    }

    fun loadMovie(id: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getMovie(id)
                if (response.isSuccessful) {
                    _movie.value = response.body()
                }
            } catch (e: Exception) {
                Log.e("MovieVM", "Error loading movie: ${e.message}")
            }
        }
    }

//    fun loadOpinions(id_movie: Int){
//        RetrofitClient.apiService.getOpinionId(id_movie).enqueue(object : Callback<List<Valoracion>> {
//            override fun onResponse(
//                call: Call<List<Valoracion>>,
//                response: Response<List<Valoracion>>
//            ) {
//                if (response.isSuccessful) {
//                    _opinions.value = response.body() ?: emptyList()
//                    loadUsersForOpinions()
//                    Log.d("valoracion", _opinions.toString())
//                } else {
//                    Log.d("valoracion else", "Error ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<List<Valoracion>>, t: Throwable) {
//                Log.e("valoracion fallo", "Error: ${t.message}")
//            }
//        })
//    }

    fun loadOpinions(peliculaId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getOpinionId(peliculaId)
                if (response.isSuccessful) {
                    val list = response.body() ?: emptyList()
                    loadUsersForOpinions(list)
                }
            } catch (e: Exception) {
                Log.e("MovieVM", "Error loading opinions: ${e.message}")
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
                Log.e("MovieVM", "Error posting opinion: ${e.message}")
            }
        }
    }

}