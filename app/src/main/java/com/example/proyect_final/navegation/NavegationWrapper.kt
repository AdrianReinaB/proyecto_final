package com.example.proyect_final.navegation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.proyect_final.ui.screens.LoginScreen
import com.example.proyect_final.ui.screens.MovieScreen
import com.example.proyect_final.ui.screens.MoviesScreen
import com.example.proyect_final.ui.screens.OpinionsScreen
import com.example.proyect_final.ui.screens.ProfileScreen
import com.example.proyect_final.ui.screens.RegisterMovieScreen
import com.example.proyect_final.ui.screens.RegisterScreen
import com.example.proyect_final.ui.screens.RentMoviesUser
import com.example.proyect_final.ui.screens.admin.ListMoviesScreen
import com.example.proyect_final.ui.screens.admin.UserListScreen
import com.example.proyect_final.ui.screens.admin.UserRentList
import com.example.proyect_final.viewModel.MovieViewModel
import com.example.proyect_final.viewModel.UserViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationWrapper() {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""
    val userViewModel: UserViewModel = viewModel()
    var isSearchActive by remember { mutableStateOf(false) }
    var isAdmin by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val hideDrawer =
        currentRoute == Login::class.qualifiedName || currentRoute == Register::class.qualifiedName

    ModalNavigationDrawer(
        scrimColor = MaterialTheme.colorScheme.scrim,
        drawerState = drawerState,
        gesturesEnabled = !hideDrawer,
        drawerContent = {
            if (!hideDrawer) {
                DrawerContent(
                    userViewModel = userViewModel,
                    isAdmin = isAdmin,
                    onIsAdmin = { newVal ->
                        isAdmin = newVal
                        if (!isAdmin) {
                            navController.navigate(Movies) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    },
                    onDestinationClicked = { route ->
                        scope.launch { drawerState.close() }
                        when (route) {
                            "Inicio" -> navController.navigate(Movies)
                            "Perfil" -> navController.navigate(Profile)
                            "Mis Comentarios" -> navController.navigate(OpinionUser)
                            "Mis Alquileres" -> navController.navigate(RentMovies)
                            "Lista de usuarios" -> navController.navigate(UsersList)
                            "Lista de peliculas" -> navController.navigate(MoviesList)
                            "Listado de alquileres" -> navController.navigate(UserListRents)
                            "Cerrar Sesión" -> {
                                isAdmin = false
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
            CenterAlignedTopAppBar(colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.scrim
            ),
                title = {
                    Text(
                        "V-club",
                        color = MaterialTheme.colorScheme.surface,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    if (!hideDrawer) {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menú",
                                tint = MaterialTheme.colorScheme.surface
                            )
                        }
                    }
                    if (currentRoute == Register::class.qualifiedName) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.surface
                            )
                        }
                    }
                },
                actions = {
                    if (currentRoute.contains("Movies")) {
                        IconButton(onClick = { isSearchActive = !isSearchActive }) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Buscar",
                                tint = MaterialTheme.colorScheme.surface
                            )
                        }
                    }
                })
        }, floatingActionButton = {
            if (currentRoute == Movies::class.qualifiedName && isAdmin) {
                FloatingActionButton(
                    onClick = { navController.navigate(RegisterMovie) },
                    containerColor = MaterialTheme.colorScheme.secondary
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.surface
                    )
                }
            }
        }, containerColor = MaterialTheme.colorScheme.onBackground) { innerPadding ->
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
                    },
                        loginViewModel = viewModel(),
                        navigateToRegister = { navController.navigate(Register) })
                }

                composable<Register> {
                    RegisterScreen(viewModel())
                }

                composable<Movies> {
                    MoviesScreen(navigateToMovie = { id -> navController.navigate(DetailMovie(id = id)) },
                        isSearchActive = isSearchActive,
                        onCloseSearch = { isSearchActive = false },
                        movieView = MovieViewModel()
                    )
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
                    RegisterMovieScreen(viewModel())
                }

                composable<RentMovies> {
                    RentMoviesUser(userViewModel, viewModel())
                }

                composable<OpinionUser> {
                    OpinionsScreen(userViewModel)
                }

                composable<UsersList> {
                    UserListScreen(userViewModel)
                }

                composable<MoviesList> {
                    ListMoviesScreen(viewModel())
                }

                composable<UserListRents> {
                    UserRentList(viewModel())
                }
            }
        }
    }
}

@Composable
fun DrawerContent(
    userViewModel: UserViewModel,
    isAdmin: Boolean,
    onIsAdmin: (Boolean) -> Unit,
    onDestinationClicked: (String) -> Unit
) {
    val user = userViewModel.currentUser.value

    ModalDrawerSheet(drawerContentColor = MaterialTheme.colorScheme.onSurface, drawerContainerColor = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Hola, ${user?.nombre}",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                if (user != null) {
                    if (user.rol.contains("admin")) {
                        IconButton(onClick = { onIsAdmin(!isAdmin) }) {
                            Icon(imageVector = Icons.Default.Build, contentDescription = "admin")
                        }
                    }
                }
            }

            HorizontalDivider()
            DrawerItem("Inicio", Icons.Default.Home) { onDestinationClicked("Inicio") }
            DrawerItem("Perfil", Icons.Default.AccountCircle) { onDestinationClicked("Perfil") }
            DrawerItem(
                "Mis Comentarios", Icons.Filled.Info
            ) { onDestinationClicked("Mis Comentarios") }
            DrawerItem(
                "Mis Alquileres", Icons.Filled.PlayArrow
            ) { onDestinationClicked("Mis Alquileres") }

            if (isAdmin) {
                HorizontalDivider()
                DrawerItem(
                    "Lista de usuarios", Icons.Filled.AccountBox
                ) { onDestinationClicked("Lista de usuarios") }
                DrawerItem(
                    "Lista de peliculas", Icons.Filled.ShoppingCart
                ) { onDestinationClicked("Lista de peliculas") }
                DrawerItem(
                    "Listado de alquileres",
                    Icons.Filled.Info
                ) { onDestinationClicked("Listado de alquileres") }
            }

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
        Icon(
            icon,
            contentDescription = text,
            modifier = Modifier.padding(end = 16.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )
        Text(text, color = MaterialTheme.colorScheme.onSurface)
    }
}
