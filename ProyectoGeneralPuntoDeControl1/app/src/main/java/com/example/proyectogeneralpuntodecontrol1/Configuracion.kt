package com.example.proyectogeneralpuntodecontrol1

import android.app.LocaleManager
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton   // importado pero no se usa (ver nota en FilaCheckbox)
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme
import com.example.proyectogeneralpuntodecontrol1.datos.ConfiguracionDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun Configuracion(onVolver: () -> Unit) {
    // BackHandler intercepta el botón/gesto "atrás" del móvil.
    // Sin esto, al pulsar atrás se cerraría la app entera (porque solo
    // hay una Activity); con esto, vuelve a la pantalla de inicio.
    BackHandler(onBack = onVolver)

    // LocalContext.current nos da el Context (la Activity) desde dentro
    // de Compose. Lo necesitamos para el DataStore, el Toast y el idioma.
    val context = LocalContext.current

    // remember: se crea una vez y se reutiliza en cada redibujado.
    val dataStore = remember { ConfiguracionDataStore(context) }

    // Un "scope" de corrutinas ligado a esta pantalla. Sirve para lanzar
    // tareas en segundo plano (como guardar) desde un onClick.
    // Si sales de la pantalla, las tareas pendientes se cancelan solas.
    val scope = rememberCoroutineScope()

    // Leemos el texto del Toast AQUÍ arriba porque stringResource es
    // @Composable y NO se puede llamar dentro del onClick (que no lo es).
    val mensajeGuardado = stringResource(R.string.config_guardado)

    // ESTADO: las variables viven aquí y se pasan
    // a las funciones de abajo (esto se llama "state hoisting":
    // "elevar el estado" al padre). Las funciones hijas solo MUESTRAN
    // el valor y AVISAN cuando el usuario lo cambia.
    // Son estados temporales: lo que el usuario va tocando. No se guarda
    // en disco hasta que pulsa "Guardar".
    var modoOscuro by remember { mutableStateOf(false) }
    var idioma by remember { mutableStateOf("es") }
    var tamanoTexto by remember { mutableStateOf("mediano") }

    // LaunchedEffect se encarga de que, al abrir la pantalla,
    // se cargue lo que había guardado en las variables.
    // - Ejecuta su bloque dentro de una corrutina (por eso puede llamar
    //   a funciones suspend como first()).
    // - La clave "Unit" nunca cambia, así que solo se ejecuta UNA vez,
    //   al entrar en la pantalla.
    // - .first() coge el primer valor del Flow y deja de escuchar
    //   (aquí no queremos escuchar cambios, solo leer una vez).
    LaunchedEffect(Unit) {
        modoOscuro = dataStore.modoOscuro.first()
        idioma = dataStore.idioma.first()
        tamanoTexto = dataStore.tamanoTexto.first()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.config_titulo),
            style = MaterialTheme.typography.displaySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        // ---------- SWITCH: modo oscuro ----------
        // Card es una "tarjeta": caja con fondo y esquinas redondeadas
        // que agrupa visualmente elementos relacionados.
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                FilaSwitch(
                    texto = stringResource(R.string.config_modo_oscuro),
                    activado = modoOscuro,
                    // "it" es el nuevo valor (true/false) que manda el Switch.
                    // Al cambiar el estado, Compose redibuja el Switch.
                    onCambio = { modoOscuro = it }
                )
            }
        }
        Spacer(Modifier.height(12.dp))

        // ---------- DESPLEGABLE: idioma ----------
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Titulo(stringResource(R.string.config_idioma))
                Desplegable(
                    // Dos listas paralelas: la posición 0 de una corresponde
                    // a la posición 0 de la otra ("es" <-> "Español").
                    claves = listOf("es", "en"),
                    textos = listOf(
                        stringResource(R.string.idioma_es),
                        stringResource(R.string.idioma_en)
                    ),
                    seleccionada = idioma,
                    onSeleccion = { idioma = it }
                )
            }
        }
        Spacer(Modifier.height(12.dp))

        // ---------- Tamaño del texto (selección única) ----------
        // Funciona como unos radio buttons: solo puede haber UNO marcado,
        // porque cada casilla está marcada solo si tamanoTexto es igual
        // a su opción (tamanoTexto == "pequeno" da true o false).
        // Al pulsar una, cambiamos tamanoTexto y las otras se desmarcan solas.
        // La lambda { tamanoTexto = "..." } va FUERA de los paréntesis:
        // en Kotlin, si el último parámetro es una función, se puede
        // escribir así ("trailing lambda").
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Titulo(stringResource(R.string.config_tamano_texto))
                FilaCheckbox(stringResource(R.string.texto_pequeno), tamanoTexto == "pequeno") { tamanoTexto = "pequeno" }
                FilaCheckbox(stringResource(R.string.texto_mediano), tamanoTexto == "mediano") { tamanoTexto = "mediano" }
                FilaCheckbox(stringResource(R.string.texto_grande), tamanoTexto == "grande") { tamanoTexto = "grande" }
            }
        }
        Spacer(Modifier.height(12.dp))

        // ---------- BOTÓN GUARDAR ----------
        Button(
            onClick = {
                // guardar() es suspend, así que necesita una corrutina.
                // scope.launch { } la crea sin bloquear la interfaz.
                scope.launch {
                    // 1. Guarda en DataStore. En cuanto termina, MainActivity
                    //    recibe los valores nuevos y aplica el tema y el tamaño.
                    dataStore.guardar(modoOscuro, idioma, tamanoTexto)

                    // Toast = mensajito flotante que desaparece solo.
                    // .show() es imprescindible; si no, no se ve.
                    Toast.makeText(context, mensajeGuardado, Toast.LENGTH_SHORT).show()

                    // 2. Cambia el idioma de la app (Android 13 o superior).
                    // Build.VERSION.SDK_INT es la versión de Android del móvil.
                    // TIRAMISU = Android 13 (API 33). LocaleManager solo existe
                    // desde ahí; en móviles más viejos la app petaría, por eso
                    // se comprueba. En Android 12 o menor, el idioma se guarda
                    // pero NO se aplica (para eso habría que usar
                    // AppCompatDelegate.setApplicationLocales de AppCompat).
                    // Al cambiar el idioma, Android RECREA la Activity para
                    // recargar los strings.xml del idioma nuevo. Gracias al
                    // rememberSaveable de PantallaPrincipal, sigues en esta pantalla.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        context.getSystemService(LocaleManager::class.java)
                            .applicationLocales = LocaleList.forLanguageTags(idioma)
                    }
                }
            },
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.config_guardar))
        }
        Spacer(Modifier.height(8.dp))

        // Volver SIN guardar: los cambios temporales se pierden porque
        // los estados con remember desaparecen al salir de la pantalla.
        OutlinedButton(
            onClick = onVolver,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.volver))
        }
    }
}

// ============ Funciones de despliegue ============
// No guardan nada: reciben el valor y avisan cuando cambia.
// Se llaman componentes "stateless" (sin estado). Ventajas:
//  - Se pueden reutilizar en cualquier pantalla.
//  - Hay una única "fuente de verdad" (el estado del padre).

// Se encarga de poner un texto grande y en negrita.
// Es un componente pequeño para no repetir el mismo estilo muchas veces.
@Composable
fun Titulo(texto: String) {
    Text(
        text = texto,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
    )
}

// Pinta una fila con un texto a la izquierda y un interruptor a la derecha.
// Esto es para el modo oscuro.
// onCambio: (Boolean) -> Unit = una función que RECIBE un Boolean.
@Composable
fun FilaSwitch(texto: String, activado: Boolean, onCambio: (Boolean) -> Unit) {
    // Row coloca a sus hijos uno al LADO del otro.
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically  // centrados en altura
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            // weight(1f) = "ocupa todo el espacio que sobre". Así el texto
            // se estira y empuja al Switch hasta el borde derecho.
            modifier = Modifier.weight(1f)
        )
        // checked = cómo se dibuja (encendido/apagado).
        // onCheckedChange = qué hacer cuando el usuario lo toca.
        // El Switch NO cambia solo: si no actualizas el estado en
        // onCambio, se queda como estaba. Así funciona todo en Compose.
        Switch(checked = activado, onCheckedChange = onCambio)
    }
}

// Pinta una casilla con su texto. Se usa para elegir el tamaño del texto.
// NOTA: para "elegir solo una opción de varias", lo habitual en Material
// Design es RadioButton (el círculo), no Checkbox (el cuadrado, que sugiere
// que puedes marcar varias). Ya tienes RadioButton importado; bastaría con
// cambiar la línea del Checkbox por:
//   RadioButton(selected = marcado, onClick = onClick)
@Composable
fun FilaCheckbox(texto: String, marcado: Boolean, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        // Ignoramos el valor que manda el Checkbox (con { onClick() })
        // porque aquí solo nos interesa saber que lo han pulsado.
        Checkbox(checked = marcado, onCheckedChange = { onClick() })
        Text(text = texto, style = MaterialTheme.typography.bodyMedium)
    }
}

// Menú desplegable genérico: sirve para cualquier lista de opciones,
// no solo para idiomas.
@Composable
fun Desplegable(
    claves: List<String>,       // lo que se guarda: "es", "en"
    textos: List<String>,       // lo que se ve: "Español", "English"
    seleccionada: String,       // la clave elegida ahora mismo
    onSeleccion: (String) -> Unit
) {
    // abierto = si la lista está desplegada o no.
    // Este estado SÍ vive aquí dentro porque es un detalle visual que
    // al padre no le importa (no hace falta "elevarlo").
    var abierto by remember { mutableStateOf(false) }

    // Buscamos en qué posición está la clave elegida para mostrar su texto.
    // Ojo: si la clave no existe, indexOf devuelve -1 y textos[-1] petaría.
    val posicion = claves.indexOf(seleccionada)

    // Box apila los hijos uno ENCIMA del otro. Se usa aquí porque el
    // DropdownMenu se coloca relativo a su padre: así aparece justo
    // debajo del botón.
    Box {
        // El botón muestra la opción actual; al pulsarlo se abre la lista.
        OutlinedButton(
            onClick = { abierto = true },
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = textos[posicion])
        }
        // expanded controla si se ve o no.
        // onDismissRequest se llama al tocar fuera del menú: lo cerramos.
        DropdownMenu(expanded = abierto, onDismissRequest = { abierto = false }) {
            // claves.indices = 0, 1, 2... (las posiciones de la lista).
            // Creamos un elemento del menú por cada opción.
            for (i in claves.indices) {
                DropdownMenuItem(
                    text = { Text(textos[i]) },
                    onClick = {
                        onSeleccion(claves[i])  // avisamos al padre con la CLAVE
                        abierto = false         // y cerramos el menú
                    }
                )
            }
        }
    }
}

@Preview(
    name = "Tema claro",
    showBackground = true
)
@Preview(
    name = "Tema oscuro",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ConfiguracionPreview() {
    AppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            // onVolver = {} -> una función vacía: en la preview no hace nada.
            Configuracion(onVolver = {})
        }
    }
}