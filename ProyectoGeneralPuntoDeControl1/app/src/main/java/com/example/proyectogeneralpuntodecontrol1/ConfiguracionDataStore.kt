package com.example.proyectogeneralpuntodecontrol1.datos

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Fichero donde se guardan las preferencias (se llama "configuracion")
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "configuracion")

class ConfiguracionDataStore(private val context: Context) {

    // CLAVES: el nombre con el que se guarda cada valor
    companion object {
        // Este guarda el tema.
        val MODO_OSCURO = booleanPreferencesKey("modo_oscuro")
        // Este guarda el idioma.
        val IDIOMA = stringPreferencesKey("idioma")
        // Este guarda el tamaño del texto
        val TAMANO_TEXTO = stringPreferencesKey("tamano_texto")
    }

    // Lee cada valor que hay. En el caso que no hay nada guardado, usa el de después de ?:
    val modoOscuro: Flow<Boolean> = context.dataStore.data.map { it[MODO_OSCURO] ?: false }
    val idioma: Flow<String> = context.dataStore.data.map { it[IDIOMA] ?: "es" }
    val tamanoTexto: Flow<String> = context.dataStore.data.map { it[TAMANO_TEXTO] ?: "mediano" }

    // Esto guarda los tres valores
    suspend fun guardar(modoOscuro: Boolean, idioma: String, tamanoTexto: String) {
        context.dataStore.edit { prefs ->
            prefs[MODO_OSCURO] = modoOscuro
            prefs[IDIOMA] = idioma
            prefs[TAMANO_TEXTO] = tamanoTexto
        }
    }
}