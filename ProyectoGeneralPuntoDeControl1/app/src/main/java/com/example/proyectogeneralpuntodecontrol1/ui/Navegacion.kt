package com.example.proyectogeneralpuntodecontrol1.ui

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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.compose.AppTheme
import com.example.proyectogeneralpuntodecontrol1.datos.ConfiguracionDataStore
import com.example.proyectogeneralpuntodecontrol1.model.Rutas

// NAVEGACIÓN: TEMA + GRÁFICO DE NAVEGACIÓN
// Esta función envuelve TODA la app, por eso aquí se hace lo que antes
// estaba en MainActivity:
// 1. DataStore: lee el modo oscuro y el tamaño de texto guardados.
//    collectAsState los escucha, así que al guardar en Configuración la app
//    cambia sola de tema y de tamaño de letra.
// 2. Escala de letra: CompositionLocalProvider multiplica el tamaño de todos
//    los textos según la opción guardada.
// 3. AppTheme + Scaffold: aplican los colores del tema y el fondo.
// 4. NavHost: es el "gráfico de navegación", el hueco donde se dibuja la
//    pantalla actual. Dentro, cada composable(ruta) { } dice qué pantalla
//    se muestra para cada ruta. startDestination es la primera pantalla.
//    Para ir a otra pantalla se usa navController.navigate(ruta), y para
//    volver, navController.popBackStack(). El botón "atrás" del móvil
//    funciona solo, porque el NavController lleva el historial.
//    innerPadding se pasa al NavHost para que nada quede debajo de las
//    barras del sistema.
// De momento la ruta Principal muestra InicioPagina. Cuando hagamos
// Principal.kt (el menú), la cambiaremos.

@Composable
fun Navegacion(navController: NavHostController) {
    val context = LocalContext.current
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
                        InicioPagina(
                            onSobreNosotros = { navController.navigate(Rutas.SobreNosotros.ruta) },
                            onAcercaDe = { navController.navigate(Rutas.AcercaDe.ruta) },
                            onConfiguracion = { navController.navigate(Rutas.Configuracion.ruta) }
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
                }
            }
        }
    }
}