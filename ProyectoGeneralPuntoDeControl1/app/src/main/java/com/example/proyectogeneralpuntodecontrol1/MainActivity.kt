// MAINACTIVITY (casi vacía)
// Es la puerta de entrada de la app y solo hace dos cosas:
// 1. Crea el controlador de navegación con rememberNavController().
//    El NavController sabe en qué pantalla estamos y guarda el historial
//    como una pila: navigate(ruta) pone una pantalla encima y
//    popBackStack() quita la de arriba para volver a la anterior.
//    remember hace que se cree una sola vez y no en cada redibujado.
// 2. Se lo pasa a Navegacion(), que es quien monta el resto de la app.
package com.example.proyectogeneralpuntodecontrol1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.proyectogeneralpuntodecontrol1.ui.Navegacion

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            Navegacion(navController)
        }
    }
}