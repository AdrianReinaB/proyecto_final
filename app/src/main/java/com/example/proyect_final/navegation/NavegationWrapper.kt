package com.example.proyect_final.navegation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyect_final.ui.screens.LoginScreen
import com.example.proyect_final.ui.screens.MoviesScreen
import com.example.proyect_final.ui.screens.RegisterScreen

@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Login
    ) {
        composable<Login> {
            LoginScreen()
        }
        composable<Register> {
            RegisterScreen()
        }
        composable<Movies> {
            MoviesScreen()
        }
    }
}