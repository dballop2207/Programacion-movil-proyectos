// DATASTORE DE CONFIGURACIÓN (paquete model)
// DataStore guarda datos pequeños (ajustes) en el móvil de forma permanente:
// aunque cierres la app o apagues el teléfono, los valores siguen ahí.
// Guarda pares CLAVE -> VALOR, como un diccionario.
// - Context.dataStore: crea el fichero "configuracion" UNA sola vez.
// - companion object: las claves (con su tipo) de cada valor guardado.
// - modoOscuro, idioma, tamanoTexto: se leen como Flow, un "grifo" que emite
//   un valor nuevo cada vez que cambia lo guardado. "?:" da el valor por
//   defecto si todavía no hay nada guardado.
// - guardar(): es suspend porque escribe en disco; se llama desde una
//   corrutina (el ViewModel). edit { } guarda los tres valores de golpe.
//   Al terminar, los Flow emiten y Navegacion.kt cambia tema y letra solo.
package com.example.proyectogeneralpuntodecontrol1.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "configuracion")

class ConfiguracionDataStore(private val context: Context) {

    companion object {
        val MODO_OSCURO = booleanPreferencesKey("modo_oscuro")
        val IDIOMA = stringPreferencesKey("idioma")
        val TAMANO_TEXTO = stringPreferencesKey("tamano_texto")
    }

    val modoOscuro: Flow<Boolean> = context.dataStore.data.map { it[MODO_OSCURO] ?: false }
    val idioma: Flow<String> = context.dataStore.data.map { it[IDIOMA] ?: "es" }
    val tamanoTexto: Flow<String> = context.dataStore.data.map { it[TAMANO_TEXTO] ?: "mediano" }

    suspend fun guardar(modoOscuro: Boolean, idioma: String, tamanoTexto: String) {
        context.dataStore.edit { prefs ->
            prefs[MODO_OSCURO] = modoOscuro
            prefs[IDIOMA] = idioma
            prefs[TAMANO_TEXTO] = tamanoTexto
        }
    }
}
