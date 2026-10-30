// El "package" indica en qué carpeta/paquete vive este archivo.
// Tiene que coincidir con el de los demás archivos (PantallaPrincipal, etc.)
// para que puedan verse entre ellos sin hacer import.
// (En el código que pegaste no aparecía; si tu archivo ya lo tiene, perfecto).
package com.example.proyectogeneralpuntodecontrol1

// ===================== IMPORTS =====================
// Los imports traen clases y funciones de librerías externas
// para poder usarlas aquí sin escribir su ruta completa.
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.example.compose.AppTheme
import com.example.proyectogeneralpuntodecontrol1.datos.ConfiguracionDataStore

// Una Activity es una "ventana" de la app. Esta es la PRIMERA que se abre
// (así está declarado en el AndroidManifest.xml con el intent-filter MAIN/LAUNCHER).
// ComponentActivity es la clase base que permite usar Jetpack Compose.
class MainActivity : ComponentActivity() {

    // onCreate es lo primero que Android ejecuta cuando se crea la Activity.
    // "override" = estamos reescribiendo un método que ya existe en la clase padre.
    // savedInstanceState trae datos guardados si la Activity se recrea
    // (por ejemplo, al girar el móvil o al cambiar de idioma).
    override fun onCreate(savedInstanceState: Bundle?) {
        // Siempre hay que llamar al onCreate original antes de hacer nada.
        super.onCreate(savedInstanceState)

        // Hace que la app dibuje "de borde a borde", es decir, también
        // por detrás de la barra de estado (arriba) y la de navegación (abajo).
        enableEdgeToEdge()

        // setContent { ... } es la puerta de entrada a Compose:
        // todo lo que pongas dentro es la interfaz que se dibuja en pantalla.
        setContent {
            // Creamos el objeto que lee/guarda la configuración en el móvil.
            // remember { } hace que se cree UNA sola vez y no en cada
            // recomposición (Compose vuelve a ejecutar este bloque muchas veces).
            // "this" aquí es la Activity, que sirve como Context.
            val dataStore = remember { ConfiguracionDataStore(this) }

            // dataStore.modoOscuro es un Flow: un "grifo" de datos que emite
            // un valor nuevo cada vez que cambia lo guardado.
            // collectAsState lo convierte en un State de Compose: cuando llega
            // un valor nuevo, Compose redibuja solo lo que lo usa.
            // initial = valor que se usa mientras aún no se ha leído el disco.
            // "by" (delegado) nos deja usar modoOscuro directamente como Boolean
            // en vez de escribir modoOscuro.value.
            val modoOscuro by dataStore.modoOscuro.collectAsState(initial = false)
            val tamanoTexto by dataStore.tamanoTexto.collectAsState(initial = "mediano")

            // "when" es como un switch: según el texto guardado,
            // devuelve un número que usaremos para multiplicar el tamaño de letra.
            // La "f" del final indica que es Float (número decimal).
            val escala = when (tamanoTexto) {
                "pequeno" -> 0.85f   // 85 % del tamaño normal
                "grande" -> 1.25f    // 125 % del tamaño normal
                else -> 1f           // cualquier otro caso ("mediano") = tamaño normal
            }

            // LocalDensity es un "valor ambiente" de Compose (CompositionLocal):
            // un dato que todos los composables de dentro pueden leer sin
            // tener que pasárselo como parámetro uno a uno.
            // Contiene dos cosas:
            //   - density: cuántos píxeles reales hay por cada dp (tamaño de las cosas)
            //   - fontScale: multiplicador para los sp (tamaño de las LETRAS)
            val densidad = LocalDensity.current

            // CompositionLocalProvider SOBRESCRIBE ese valor ambiente para todo
            // lo que hay dentro de sus llaves. Dejamos density igual (los
            // elementos no cambian de tamaño) y multiplicamos fontScale por
            // nuestra escala, así TODOS los textos de la app crecen o encogen
            // de golpe, sin tocar cada Text() por separado.
            CompositionLocalProvider(
                LocalDensity provides Density(densidad.density, densidad.fontScale * escala)
            ) {
                // AppTheme es tu tema (colores, tipografías, formas) de Material 3.
                // Le pasamos darkTheme para que use la paleta clara u oscura
                // según lo que el usuario guardó en Configuración.
                AppTheme(darkTheme = modoOscuro) {
                    // Scaffold es el "esqueleto" típico de una pantalla Material:
                    // admite barra superior, inferior, botón flotante... Aquí
                    // solo lo usamos para el fondo y el relleno de los bordes.
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),  // ocupa toda la pantalla
                        containerColor = MaterialTheme.colorScheme.background
                    ) { innerPadding ->
                        // innerPadding es el hueco que hay que dejar para no
                        // quedar tapado por las barras del sistema (por el
                        // enableEdgeToEdge de arriba). OJO: ahora mismo NO se usa,
                        // así que el contenido podría quedar debajo de la barra
                        // de estado. Para usarlo podrías hacer:
                        //   Box(Modifier.padding(innerPadding)) { PantallaPrincipal() }
                        PantallaPrincipal()
                    }
                }
            }
        }
    }
}