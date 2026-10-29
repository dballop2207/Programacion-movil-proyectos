package com.example.proyectogeneralpuntodecontrol1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider   // ←
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity           // ←
import androidx.compose.ui.unit.Density                    // ←
import com.example.compose.AppTheme
import com.example.proyectogeneralpuntodecontrol1.datos.ConfiguracionDataStore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            //Creamos él data que se encarga de poner los datos por defecto
            //sobre la configuración de la aplicación
            val dataStore = remember { ConfiguracionDataStore(this) }
            val modoOscuro by dataStore.modoOscuro.collectAsState(initial = false)
            val tamanoTexto by dataStore.tamanoTexto.collectAsState(initial = "mediano")   // ←

            // Convierte la opción guardada en un multiplicador del tamaño de letra
            val escala = when (tamanoTexto) {
                "pequeno" -> 0.85f
                "grande" -> 1.25f
                else -> 1f
            }

            // Aplica ese multiplicador a todos los textos de la app
            val densidad = LocalDensity.current
            CompositionLocalProvider(
                LocalDensity provides Density(densidad.density, densidad.fontScale * escala)
            ) {
                AppTheme(darkTheme = modoOscuro) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = MaterialTheme.colorScheme.background
                    ) { innerPadding ->
                        PantallaPrincipal()
                    }
                }
            }
        }
    }
}