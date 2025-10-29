package com.example.proyect_final.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun MoviesScreen(){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        LazyVerticalGrid(columns = GridCells.Fixed(3), contentPadding = PaddingValues(10.dp)) {
            item {
                ProductCard("nombre peli", "https://th.bing.com/th/id/R.6bfebb09fd003f700f38c1f8da8155f0?rik=QFDbzC9mAqai3g&pid=ImgRaw&r=0")
            }
            item {
                ProductCard("peli", "https://i.pinimg.com/originals/82/d2/5b/82d25b943e9c7a1f9e801f498461d4f6.jpg")
            }
            item {
                ProductCard("peli", "https://www.laguiadelvaron.com/wp-content/uploads/2019/07/portadas-pel%C3%ADculas-iguales-www.laguiadelvaron-15.jpg")
            }
            item {
                ProductCard("nombre peli", "https://th.bing.com/th/id/R.6bfebb09fd003f700f38c1f8da8155f0?rik=QFDbzC9mAqai3g&pid=ImgRaw&r=0")
            }
            item {
                ProductCard("peli", "https://i.pinimg.com/originals/82/d2/5b/82d25b943e9c7a1f9e801f498461d4f6.jpg")
            }
            item {
                ProductCard("peli", "https://www.laguiadelvaron.com/wp-content/uploads/2019/07/portadas-pel%C3%ADculas-iguales-www.laguiadelvaron-15.jpg")
            }
            item {
                ProductCard("nombre peli", "https://th.bing.com/th/id/R.6bfebb09fd003f700f38c1f8da8155f0?rik=QFDbzC9mAqai3g&pid=ImgRaw&r=0")
            }
            item {
                ProductCard("peli", "https://i.pinimg.com/originals/82/d2/5b/82d25b943e9c7a1f9e801f498461d4f6.jpg")
            }
            item {
                ProductCard("peli", "https://www.laguiadelvaron.com/wp-content/uploads/2019/07/portadas-pel%C3%ADculas-iguales-www.laguiadelvaron-15.jpg")
            }
            item {
                ProductCard("nombre peli", "https://th.bing.com/th/id/R.6bfebb09fd003f700f38c1f8da8155f0?rik=QFDbzC9mAqai3g&pid=ImgRaw&r=0")
            }
            item {
                ProductCard("peli", "https://i.pinimg.com/originals/82/d2/5b/82d25b943e9c7a1f9e801f498461d4f6.jpg")
            }
            item {
                ProductCard("peli", "https://www.laguiadelvaron.com/wp-content/uploads/2019/07/portadas-pel%C3%ADculas-iguales-www.laguiadelvaron-15.jpg")
            }
            item {
                ProductCard("nombre peli", "https://th.bing.com/th/id/R.6bfebb09fd003f700f38c1f8da8155f0?rik=QFDbzC9mAqai3g&pid=ImgRaw&r=0")
            }
            item {
                ProductCard("peli", "https://i.pinimg.com/originals/82/d2/5b/82d25b943e9c7a1f9e801f498461d4f6.jpg")
            }
            item {
                ProductCard("peli", "https://www.laguiadelvaron.com/wp-content/uploads/2019/07/portadas-pel%C3%ADculas-iguales-www.laguiadelvaron-15.jpg")
            }
        }
    }
}

@Composable
fun ProductCard(name: String, url: String) {
    Box(modifier = Modifier.clip(RoundedCornerShape(5.dp)).padding(5.dp).fillMaxSize().background(
        Color.LightGray), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
            AsyncImage(
                model = url,
                contentDescription = "",
                modifier = Modifier.padding(5.dp).size(150.dp)
            )
            Text(name, modifier = Modifier.padding(5.dp))
        }
    }
}