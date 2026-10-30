// Este archivo está en un subpaquete ".datos" para separar la lógica
// de guardar datos de la lógica de pintar pantallas. Es buena práctica.
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

// ¿QUÉ ES DATASTORE?
// Es la forma moderna de guardar datos pequeños (ajustes, preferencias)
// en el móvil de forma permanente: aunque cierres la app o apagues el
// teléfono, los valores siguen ahí. Sustituye a las antiguas SharedPreferences.
// Guarda pares CLAVE -> VALOR, como un diccionario.

// Esta línea es una "extension property": le añadimos a TODOS los Context
// una propiedad nueva llamada dataStore.
// "by preferencesDataStore(...)" es un delegado que se encarga de crear
// el fichero UNA sola vez (patrón singleton). Es importante que esté fuera
// de la clase y sea private: si se creasen dos DataStore sobre el mismo
// fichero, la app fallaría.
// name = "configuracion" -> el fichero en el móvil se llamará configuracion.preferences_pb
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "configuracion")

// La clase recibe un Context porque para llegar a los ficheros de la app
// siempre hace falta uno (la Activity es un Context).
// "private val" en el constructor = lo guarda como propiedad de la clase.
class ConfiguracionDataStore(private val context: Context) {

    // companion object es lo que en Java sería "static": cosas que pertenecen
    // a la clase en sí, no a cada objeto. Las claves son siempre las mismas,
    // así que tiene sentido que sean compartidas.
    companion object {
        // Cada clave tiene un TIPO (boolean, string, int...) y un NOMBRE.
        // El tipo garantiza que no puedas guardar un texto donde va un Boolean.
        // Este guarda el tema (true = oscuro, false = claro).
        val MODO_OSCURO = booleanPreferencesKey("modo_oscuro")
        // Este guarda el idioma ("es" o "en").
        val IDIOMA = stringPreferencesKey("idioma")
        // Este guarda el tamaño del texto ("pequeno", "mediano" o "grande").
        val TAMANO_TEXTO = stringPreferencesKey("tamano_texto")
    }

    // ===================== LEER =====================
    // context.dataStore.data es un Flow<Preferences>: emite TODAS las
    // preferencias cada vez que alguna cambia.
    // .map { ... } transforma cada emisión: de todas las preferencias,
    // nos quedamos solo con el valor de UNA clave.
    // "it" es el nombre automático del parámetro de la lambda (las Preferences).
    // it[MODO_OSCURO] puede ser null si todavía no se ha guardado nada,
    // y el operador Elvis "?:" dice: "si es null, usa este valor por defecto".
    val modoOscuro: Flow<Boolean> = context.dataStore.data.map { it[MODO_OSCURO] ?: false }
    val idioma: Flow<String> = context.dataStore.data.map { it[IDIOMA] ?: "es" }
    val tamanoTexto: Flow<String> = context.dataStore.data.map { it[TAMANO_TEXTO] ?: "mediano" }

    // ===================== GUARDAR =====================
    // "suspend" significa que esta función puede tardar (escribe en disco)
    // y por eso solo se puede llamar desde una CORRUTINA (scope.launch { }).
    // Así no se bloquea la interfaz mientras se guarda.
    suspend fun guardar(modoOscuro: Boolean, idioma: String, tamanoTexto: String) {
        // edit { } abre una "transacción": todos los cambios de dentro
        // se guardan juntos de una vez (o ninguno si algo falla).
        // prefs es una versión editable de las preferencias.
        context.dataStore.edit { prefs ->
            prefs[MODO_OSCURO] = modoOscuro
            prefs[IDIOMA] = idioma
            prefs[TAMANO_TEXTO] = tamanoTexto
        }
        // En cuanto termina edit, los Flow de arriba emiten los valores nuevos
        // y MainActivity (que los está escuchando con collectAsState) se
        // redibuja sola con el tema y tamaño nuevos. ¡Magia reactiva!
    }
}