// VIEWMODEL DE CONFIGURACIÓN (MVVM)
// Separa la lógica de la pantalla: Configuracion.kt solo dibuja y avisa,
// y aquí se guarda el estado y se decide qué hacer.
//   Vista (Configuracion.kt) <-> ViewModel <-> Modelo (ConfiguracionDataStore)
// - modoOscuro, idioma, tamanoTexto: lo que el usuario está editando.
//   "private set" hace que la pantalla pueda leerlos pero no cambiarlos
//   directamente; tiene que usar las funciones cambiar...().
// - modoOscuroGuardado, idiomaGuardado, tamanoGuardado: copia de lo que hay
//   en disco. Sirven para la COMPROBACIÓN: comparar lo editado con lo guardado.
// - cargar(): lee el DataStore y rellena las dos copias.
// - hayCambios(): devuelve true si algún valor editado es distinto del guardado.
// - guardar(onResultado): si no hay cambios, avisa con false y no hace nada.
//   Si hay cambios, guarda en el DataStore, actualiza la copia guardada,
//   avisa con true y cambia el idioma (Android 13+).
//   onResultado es una función que le pasa la pantalla para enterarse del
//   resultado y mostrar el toast y navegar (eso es cosa de la vista).
// - viewModelScope: corrutinas que viven lo mismo que el ViewModel, para
//   poder leer y escribir en disco sin bloquear la pantalla.
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
import com.example.proyectogeneralpuntodecontrol1.model.ConfiguracionDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// Los tres resultados posibles al pulsar Guardar.
enum class ResultadoGuardado { GUARDADO, SIN_CAMBIOS, COMBINACION_NO_VALIDA }
class ConfiguracionViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = ConfiguracionDataStore(application)

    var modoOscuro by mutableStateOf(false)
        private set
    var idioma by mutableStateOf("es")
        private set
    var tamanoTexto by mutableStateOf("mediano")
        private set

    private var modoOscuroGuardado = false
    private var idiomaGuardado = "es"
    private var tamanoGuardado = "mediano"

    fun cargar() {
        viewModelScope.launch {
            modoOscuroGuardado = dataStore.modoOscuro.first()
            idiomaGuardado = dataStore.idioma.first()
            tamanoGuardado = dataStore.tamanoTexto.first()

            modoOscuro = modoOscuroGuardado
            idioma = idiomaGuardado
            tamanoTexto = tamanoGuardado
        }
    }

    fun cambiarModoOscuro(valor: Boolean) {
        modoOscuro = valor
    }

    fun cambiarIdioma(valor: String) {
        idioma = valor
    }

    fun cambiarTamanoTexto(valor: String) {
        tamanoTexto = valor
    }

    private fun hayCambios(): Boolean =
        modoOscuro != modoOscuroGuardado ||
                idioma != idiomaGuardado ||
                tamanoTexto != tamanoGuardado

    // Combinación prohibida: modo oscuro + texto pequeño (se lee peor).
    private fun combinacionValida(): Boolean =
        !(modoOscuro && tamanoTexto == "pequeno")

    fun guardar(onResultado: (ResultadoGuardado) -> Unit) {
        if (!hayCambios()) {
            onResultado(ResultadoGuardado.SIN_CAMBIOS)
            return
        }
        if (!combinacionValida()) {
            onResultado(ResultadoGuardado.COMBINACION_NO_VALIDA)
            return
        }

        viewModelScope.launch {
            dataStore.guardar(modoOscuro, idioma, tamanoTexto)

            modoOscuroGuardado = modoOscuro
            idiomaGuardado = idioma
            tamanoGuardado = tamanoTexto

            onResultado(ResultadoGuardado.GUARDADO)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getApplication<Application>().getSystemService(LocaleManager::class.java)
                    .applicationLocales = LocaleList.forLanguageTags(idioma)
            }
        }
    }
}
