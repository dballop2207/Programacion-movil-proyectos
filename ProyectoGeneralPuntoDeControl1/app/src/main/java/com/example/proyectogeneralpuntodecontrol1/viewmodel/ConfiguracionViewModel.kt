package com.example.proyectogeneralpuntodecontrol1.viewmodel

import android.app.Application
import android.app.LocaleManager
import android.os.Build
import android.os.LocaleList
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectogeneralpuntodecontrol1.datos.ConfiguracionDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// ¿QUÉ ES UN VIEWMODEL?
// Es una clase intermediaria entre las PANTALLAS y los DATOS.
//   Pantalla  <-->  ViewModel  <-->  DataStore
// - La pantalla solo dibuja y avisa de lo que toca el usuario.
// - El ViewModel guarda el estado y decide qué hacer con él.
// - El DataStore guarda en disco.
//
// Ventaja clave: el ViewModel SOBREVIVE cuando Android recrea la Activity
// (al girar el móvil o cambiar de idioma). Los "remember" se perderían.
//
// AndroidViewModel es un ViewModel que recibe la Application, que nos
// sirve como Context para crear el DataStore y para cambiar el idioma.
class ConfiguracionViewModel(application: Application) : AndroidViewModel(application) {

    // El ViewModel es el ÚNICO que habla con el DataStore.
    // "private" = las pantallas no pueden tocarlo directamente.
    private val dataStore = ConfiguracionDataStore(application)

    // ============ VALORES EN EDICIÓN (los usa la pantalla Configuracion) ============
    // Es lo que el usuario va tocando en la pantalla, ANTES de pulsar "Guardar".
    // Antes estaban en la pantalla con remember; ahora viven aquí.
    //
    // "private set" = desde fuera se pueden LEER pero no cambiar.
    // Para cambiarlos hay que llamar a las funciones de abajo.
    // Así todo cambio pasa por el ViewModel (una única puerta de entrada).
    var modoOscuro by mutableStateOf(false)
        private set
    var idioma by mutableStateOf("es")
        private set
    var tamanoTexto by mutableStateOf("mediano")
        private set

    // Carga en los valores de edición lo que hay guardado en disco.
    // Se llama al entrar en la pantalla de Configuración, para que si
    // el usuario tocó cosas y salió SIN guardar, al volver vea lo guardado.
    // viewModelScope.launch crea una corrutina para poder usar first(),
    // que es suspend (tiene que esperar a leer el disco).
    fun cargar() {
        viewModelScope.launch {
            modoOscuro = dataStore.modoOscuro.first()
            idioma = dataStore.idioma.first()
            tamanoTexto = dataStore.tamanoTexto.first()
        }
    }

    // Funciones para cambiar cada valor. La pantalla las llama
    // cuando el usuario toca el Switch, el desplegable o las casillas.
    fun cambiarModoOscuro(valor: Boolean) { modoOscuro = valor }
    fun cambiarIdioma(valor: String) { idioma = valor }
    fun cambiarTamanoTexto(valor: String) { tamanoTexto = valor }

    // Guarda los valores de edición en disco.
    // onGuardado es una función que la pantalla nos pasa para enterarse
    // de que ya se ha guardado (y mostrar el Toast, que es cosa de la pantalla).
    fun guardar(onGuardado: () -> Unit) {
        // Ya no hace falta rememberCoroutineScope en la pantalla:
        // el ViewModel tiene su propio scope.
        viewModelScope.launch {
            // 1. Guarda en DataStore. Al terminar, los Flow del DataStore
            //    emiten los valores nuevos y MainActivity (que los escucha
            //    con collectAsState) cambia el tema y el tamaño de letra.
            dataStore.guardar(modoOscuro, idioma, tamanoTexto)
            onGuardado()

            // 2. Cambia el idioma de la app (solo Android 13 o superior).
            // getApplication() nos da el Context guardado en el AndroidViewModel.
            // Android recreará la Activity para cargar los textos del nuevo
            // idioma, pero este ViewModel sobrevive, así que no se pierde nada.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getApplication<Application>().getSystemService(LocaleManager::class.java)
                    .applicationLocales = LocaleList.forLanguageTags(idioma)
            }
        }
    }
}