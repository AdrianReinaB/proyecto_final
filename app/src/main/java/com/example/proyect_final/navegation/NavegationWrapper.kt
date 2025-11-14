package com.example.proyect_final.navegation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyect_final.ui.screens.LoginScreen
import com.example.proyect_final.ui.screens.RegisterScreen
import com.example.proyect_final.ui.screens.MovieScreen
import com.example.proyect_final.ui.screens.MoviesScreen
import com.example.proyect_final.ui.screens.ProfileScreen
import com.example.proyect_final.ui.screens.RegisterMovieScreen
import com.example.proyect_final.ui.screens.RentMoviesUser
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""
    val userViewModel: UserViewModel = viewModel()
    var isSearchActive by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val hideDrawer =
        currentRoute == Login::class.qualifiedName || currentRoute == Register::class.qualifiedName


    ModalNavigationDrawer(drawerState = drawerState, gesturesEnabled = !hideDrawer, drawerContent = {
        if (!hideDrawer) {
            DrawerContent(userViewModel = userViewModel, onDestinationClicked = { route ->
                scope.launch { drawerState.close() }
                when (route) {
                    "Inicio" -> navController.navigate(Movies)
                    "Perfil" -> navController.navigate(Profile)
                    "Mis Alquileres" -> navController.navigate(RentMovies)
                    "Cerrar Sesión" -> {
                        userViewModel.clearUser()
                        navController.navigate(Login) {
                            popUpTo(0)
                        }
                    }
                }
            })
        }
    }) {
        Scaffold(topBar = {
            CenterAlignedTopAppBar(title = { Text("Videoclub") }, navigationIcon = {
                if (!hideDrawer) {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(
                            imageVector = Icons.Default.Menu, contentDescription = "Menú"
                        )
                    }
                }
                if (currentRoute == Register::class.qualifiedName) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "")
                    }
                }
            }, actions = {
                if (currentRoute.contains("Movies")) {
                    IconButton(onClick = { isSearchActive = !isSearchActive }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }
                }
            })
        }, floatingActionButton = {
            if (currentRoute == Movies::class.qualifiedName) {
                FloatingActionButton(
                    onClick = { navController.navigate(RegisterMovie) },
                    containerColor = MaterialTheme.colorScheme.onSecondary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "")
                }
            }
        }) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Login,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable<Login> {
                    LoginScreen(onLoginSuccess = { usuario ->
                        userViewModel.setUser(usuario)
                        navController.navigate(Movies) {
                            popUpTo(Login) { inclusive = true }
                        }
                    }, navigateToRegister = { navController.navigate(Register) })
                }

                composable<Register> {
                    RegisterScreen()
                }

                composable<Movies> {
                    MoviesScreen(navigateToMovie = { id -> navController.navigate(DetailMovie(id = id)) },
                        isSearchActive = isSearchActive,
                        onCloseSearch = { isSearchActive = false })
                }

                composable<DetailMovie> { backStackEntry ->
                    val detail: DetailMovie = backStackEntry.toRoute()
                    MovieScreen(detail.id, userViewModel = userViewModel)
                }

                composable<Profile> {
                    ProfileScreen(userViewModel = userViewModel,
                        navigateToMovie = { id -> navController.navigate(DetailMovie(id = id)) })
                }

                composable<RegisterMovie> {
                    RegisterMovieScreen()
                }

                composable<RentMovies>{
                    RentMoviesUser(userViewModel)
                }
            }
        }
    }
}

@Composable
fun DrawerContent(
    userViewModel: UserViewModel, onDestinationClicked: (String) -> Unit
) {
    val user = userViewModel.currentUser.value

    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Hola, ${user?.nombre}",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            HorizontalDivider()
            DrawerItem("Inicio", Icons.Default.Home) { onDestinationClicked("Inicio") }
            DrawerItem("Perfil", Icons.Default.AccountCircle) { onDestinationClicked("Perfil") }
            DrawerItem(
                "Mis Comentarios", Icons.Filled.Info
            ) { onDestinationClicked("Mis Comentarios") }
            DrawerItem(
                "Mis Alquileres", Icons.Filled.PlayArrow
            ) { onDestinationClicked("Mis Alquileres") }
            HorizontalDivider()
//            DrawerItem(
//                "Lista de usuarios", Icons.Filled.AccountBox
//            ) { onDestinationClicked("Lista de usuarios") }

            Spacer(modifier = Modifier.weight(1f))

            HorizontalDivider()
            DrawerItem("Cerrar Sesión", Icons.Default.AccountCircle) {
                onDestinationClicked("Cerrar Sesión")
            }
        }
    }
}

@Composable
fun DrawerItem(
    text: String, icon: ImageVector, onClick: () -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Start) {
        Icon(icon, contentDescription = text, modifier = Modifier.padding(end = 16.dp))
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}
