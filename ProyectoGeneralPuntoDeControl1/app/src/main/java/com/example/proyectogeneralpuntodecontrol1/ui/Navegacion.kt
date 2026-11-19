// Navegacion: lee el DataStore (modo oscuro y tamaño de letra), aplica el tema y monta el NavHost con todas las pantallas.
// authViewModel se crea UNA vez aquí y se pasa a Principal, InicioSesion y Registro, así todas ven la misma sesión.
// Principal escucha el estado del login para mostrar el correo y el botón de iniciar/cerrar sesión.
// popBackStack(Rutas.Principal.ruta, inclusive = false) quita login y registro y te deja en el menú.
// onVolver = popBackStack(): de Login vuelves a Principal y de Registro vuelves a Login.
package com.example.proyectogeneralpuntodecontrol1.ui

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.compose.AppTheme
import com.example.proyectogeneralpuntodecontrol1.datos.ConfiguracionDataStore
import com.example.proyectogeneralpuntodecontrol1.model.Rutas
import com.example.proyectogeneralpuntodecontrol1.ui.authentication.InicioSesion
import com.example.proyectogeneralpuntodecontrol1.ui.authentication.Registro
import com.example.proyectogeneralpuntodecontrol1.viewmodel.AuthViewModel
import com.example.proyectogeneralpuntodecontrol1.viewmodel.EstadoAuth

@Composable
fun Navegacion(navController: NavHostController) {
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel()
    val dataStore = remember { ConfiguracionDataStore(context) }
    val modoOscuro by dataStore.modoOscuro.collectAsState(initial = false)
    val tamanoTexto by dataStore.tamanoTexto.collectAsState(initial = "mediano")

    val escala = when (tamanoTexto) {
        "pequeno" -> 0.85f
        "grande" -> 1.25f
        else -> 1f
    }

    val densidad = LocalDensity.current
    CompositionLocalProvider(
        LocalDensity provides Density(densidad.density, densidad.fontScale * escala)
    ) {
        AppTheme(darkTheme = modoOscuro) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = MaterialTheme.colorScheme.background
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = Rutas.Principal.ruta,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable(Rutas.Principal.ruta) {
                        val estado by authViewModel.estado.collectAsState()
                        Principal(
                            conSesion = estado is EstadoAuth.ConSesion,
                            correo = authViewModel.correoUsuario,
                            onIniciarSesion = {
                                authViewModel.comprobarSesion()
                                navController.navigate(Rutas.InicioSesion.ruta)
                            },
                            onCerrarSesion = { authViewModel.cerrarSesion() },
                            onAcercaDe = { navController.navigate(Rutas.AcercaDe.ruta) },
                            onSobreNosotros = { navController.navigate(Rutas.SobreNosotros.ruta) },
                            onConfiguracion = { navController.navigate(Rutas.Configuracion.ruta) },
                            onPokedex = { navController.navigate(Rutas.Pokedex.ruta) },
                            onSalir = { (context as Activity).finish() }
                        )
                    }
                    composable(Rutas.SobreNosotros.ruta) {
                        SobreNosotros(onVolver = { navController.popBackStack() })
                    }
                    composable(Rutas.AcercaDe.ruta) {
                        AcercaDe(onVolver = { navController.popBackStack() })
                    }
                    composable(Rutas.Configuracion.ruta) {
                        Configuracion(onVolver = { navController.popBackStack() })
                    }
                    composable(Rutas.Pokedex.ruta) {
                        Pokedex(onVolver = { navController.popBackStack() })
                    }
                    composable(Rutas.InicioSesion.ruta) {
                        InicioSesion(
                            viewModel = authViewModel,
                            onSesionIniciada = { navController.popBackStack(Rutas.Principal.ruta, inclusive = false) },
                            onIrARegistro = {
                                authViewModel.comprobarSesion()
                                navController.navigate(Rutas.Registro.ruta)
                            },
                            onVolver = { navController.popBackStack() }
                        )
                    }
                    composable(Rutas.Registro.ruta) {
                        Registro(
                            viewModel = authViewModel,
                            onSesionIniciada = { navController.popBackStack(Rutas.Principal.ruta, inclusive = false) },
                            onIrALogin = {
                                authViewModel.comprobarSesion()
                                navController.popBackStack()
                            },
                            onVolver = { navController.popBackStack(Rutas.Principal.ruta, inclusive = false) }
                        )
                    }
                }
            }
        }
    }
}