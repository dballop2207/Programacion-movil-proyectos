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
import androidx.compose.material3.RadioButton
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
    BackHandler(onBack = onVolver)

    val context = LocalContext.current
    val dataStore = remember { ConfiguracionDataStore(context) }
    val scope = rememberCoroutineScope()
    val mensajeGuardado = stringResource(R.string.config_guardado)

    // ESTADO: las variables viven aquí y se pasan
    // a las funciones de abajo (state hoisting)
    var modoOscuro by remember { mutableStateOf(false) }
    var idioma by remember { mutableStateOf("es") }
    var tamanoTexto by remember { mutableStateOf("mediano") }

    // launchedEffect se ecarga de que al abrir la pantalla,
    // carga lo que había guardado en las variables.
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

        // SWITCH: modo oscuro
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                FilaSwitch(
                    texto = stringResource(R.string.config_modo_oscuro),
                    activado = modoOscuro,
                    onCambio = { modoOscuro = it }
                )
            }
        }
        Spacer(Modifier.height(12.dp))

        // DESPLEGABLE: idioma
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Titulo(stringResource(R.string.config_idioma))
                Desplegable(
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

        // RADIO BUTTONS: tamaño del texto
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Titulo(stringResource(R.string.config_tamano_texto))
                FilaCheckbox(stringResource(R.string.texto_pequeno), tamanoTexto == "pequeno") { tamanoTexto = "pequeno" }
                FilaCheckbox(stringResource(R.string.texto_mediano), tamanoTexto == "mediano") { tamanoTexto = "mediano" }
                FilaCheckbox(stringResource(R.string.texto_grande), tamanoTexto == "grande") { tamanoTexto = "grande" }
            }
        }
        Spacer(Modifier.height(12.dp))

        // BOTÓN GUARDAR
        Button(
            onClick = {
                scope.launch {
                    // 1. Guarda en DataStore
                    dataStore.guardar(modoOscuro, idioma, tamanoTexto)
                    Toast.makeText(context, mensajeGuardado, Toast.LENGTH_SHORT).show()

                    // 2. Cambia el idioma de la app (Android 13 o superior)
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

@Composable
// Se encarga de poner a un texto que sea
// grande y que tenga negrita.
fun Titulo(texto: String) {
    Text(
        text = texto,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
    )
}

@Composable
// Pinta una fila con un texto a la izquierda
// y un interruptor a la derecha.
// Esto es para el modo oscuro
fun FilaSwitch(texto: String, activado: Boolean, onCambio: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Switch(checked = activado, onCheckedChange = onCambio)
    }
}

//
@Composable
fun FilaCheckbox(texto: String, marcado: Boolean, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = marcado, onCheckedChange = { onClick() })
        Text(text = texto, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun Desplegable(
    claves: List<String>,       // lo que se guarda: "es", "en"
    textos: List<String>,       // lo que se ve: "Español", "English"
    seleccionada: String,
    onSeleccion: (String) -> Unit
) {
    // abierto = si la lista está desplegada o no
    var abierto by remember { mutableStateOf(false) }
    val posicion = claves.indexOf(seleccionada)

    Box {
        OutlinedButton(
            onClick = { abierto = true },
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = textos[posicion])
        }
        DropdownMenu(expanded = abierto, onDismissRequest = { abierto = false }) {
            for (i in claves.indices) {
                DropdownMenuItem(
                    text = { Text(textos[i]) },
                    onClick = {
                        onSeleccion(claves[i])
                        abierto = false
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
            Configuracion(onVolver = {})
        }
    }
}