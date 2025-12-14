package com.example.proyect_final

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.example.proyect_final.navegation.NavigationWrapper
import com.example.proyect_final.ui.theme.Proyect_finalTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Proyect_finalTheme {
               NavigationWrapper()
            }
        }
    }
}