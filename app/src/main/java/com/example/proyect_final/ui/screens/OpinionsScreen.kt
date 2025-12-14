package com.example.proyect_final.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyect_final.R
import com.example.proyect_final.utility.toDate
import com.example.proyect_final.utility.toDateString
import com.example.proyect_final.viewModel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OpinionsScreen(userViewModel: UserViewModel){

    val currentUser = userViewModel.currentUser.value
    val opinions by userViewModel.opinionForUser.collectAsState()

    LaunchedEffect(Unit) {
        currentUser?.let {
            userViewModel.loadOpinionForUser(it.id_usuario)
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            if (!opinions.isNullOrEmpty()){
                items(opinions!!) { op ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)) {
                            Text(op.titulo,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface)
                            Text(toDateString(toDate(op.fecha)),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface)
                        }
                        Text(stringResource(R.string.Puntuacion)+": "+op.puntuacion, modifier = Modifier.padding(horizontal = 12.dp),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface)
                        Text(
                            text = op.comentario,
                            modifier = Modifier.padding(12.dp),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

        }
    }



}