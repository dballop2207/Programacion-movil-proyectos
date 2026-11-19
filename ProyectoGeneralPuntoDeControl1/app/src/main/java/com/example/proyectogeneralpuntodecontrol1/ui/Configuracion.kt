package com.example.proyectogeneralpuntodecontrol1.ui

import android.content.res.Configuration
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.AppTheme
import com.example.proyectogeneralpuntodecontrol1.viewmodel.ConfiguracionViewModel
import com.example.proyectogeneralpuntodecontrol1.R

// ============ PANTALLA CON VIEWMODEL ============
// La pantalla ahora se divide en DOS funciones:
//  1. Configuracion: conecta con el ViewModel (tiene "estado").
//  2. ConfiguracionContenido: solo dibuja (no tiene estado).
// Así la parte que dibuja se puede previsualizar sin ViewModel.

// viewModel() es una función de Compose que nos da el ViewModel:
// la primera vez lo crea y las siguientes devuelve el MISMO.
// Ponerlo como parámetro con valor por defecto permite pasar otro
// distinto si algún día hace falta (por ejemplo, en pruebas).
@Composable
fun Configuracion(
    onVolver: () -> Unit,
    viewModel: ConfiguracionViewModel = viewModel()
) {
    // El botón/gesto "atrás" del móvil vuelve a inicio en vez de cerrar la app.
    BackHandler(onBack = onVolver)

    // Hace falta para el Toast.
    val context = LocalContext.current

    // Se lee aquí arriba porque stringResource es @Composable y no se
    // puede llamar dentro de onGuardar (que no lo es).
    val mensajeGuardado = stringResource(R.string.config_guardado)

    // Al entrar en la pantalla, pedimos al ViewModel que cargue lo guardado.
    // Antes este LaunchedEffect leía el DataStore directamente;
    // ahora solo le dice al ViewModel "carga", y él se encarga.
    LaunchedEffect(Unit) {
        viewModel.cargar()
    }

    // Le pasamos a la parte visual los valores del ViewModel y las
    // funciones para cambiarlos.
    // viewModel::cambiarModoOscuro es una "referencia a función":
    // pasamos la función en sí, sin ejecutarla. Equivale a escribir
    // { viewModel.cambiarModoOscuro(it) }.
    ConfiguracionContenido(
        modoOscuro = viewModel.modoOscuro,
        idioma = viewModel.idioma,
        tamanoTexto = viewModel.tamanoTexto,
        onModoOscuroCambio = viewModel::cambiarModoOscuro,
        onIdiomaCambio = viewModel::cambiarIdioma,
        onTamanoTextoCambio = viewModel::cambiarTamanoTexto,
        onGuardar = {
            // Le pedimos al ViewModel que guarde, y cuando termine
            // mostramos el Toast (mostrar mensajes es cosa de la pantalla).
            viewModel.guardar {
                Toast.makeText(context, mensajeGuardado, Toast.LENGTH_SHORT).show()
            }
        },
        onVolver = onVolver
    )
}

// ============ PARTE VISUAL (sin estado) ============
// Recibe los valores y avisa cuando cambian. No sabe que existe
// un ViewModel ni un DataStore: solo dibuja.
@Composable
fun ConfiguracionContenido(
    modoOscuro: Boolean,
    idioma: String,
    tamanoTexto: String,
    onModoOscuroCambio: (Boolean) -> Unit,
    onIdiomaCambio: (String) -> Unit,
    onTamanoTextoCambio: (String) -> Unit,
    onGuardar: () -> Unit,
    onVolver: () -> Unit
) {
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
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                FilaSwitch(
                    texto = stringResource(R.string.config_modo_oscuro),
                    activado = modoOscuro,
                    onCambio = onModoOscuroCambio
                )
            }
        }
        Spacer(Modifier.height(12.dp))

        // ---------- DESPLEGABLE: idioma ----------
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Titulo(stringResource(R.string.config_idioma))
                Desplegable(
                    // Dos listas paralelas: posición 0 = "es" = "Español".
                    claves = listOf("es", "en"),
                    textos = listOf(
                        stringResource(R.string.idioma_es),
                        stringResource(R.string.idioma_en)
                    ),
                    seleccionada = idioma,
                    onSeleccion = onIdiomaCambio
                )
            }
        }
        Spacer(Modifier.height(12.dp))

        // ---------- Tamaño del texto (selección única) ----------
        // Cada casilla está marcada solo si tamanoTexto coincide con su opción,
        // así que solo puede haber una marcada a la vez.
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Titulo(stringResource(R.string.config_tamano_texto))
                FilaCheckbox(stringResource(R.string.texto_pequeno), tamanoTexto == "pequeno") { onTamanoTextoCambio("pequeno") }
                FilaCheckbox(stringResource(R.string.texto_mediano), tamanoTexto == "mediano") { onTamanoTextoCambio("mediano") }
                FilaCheckbox(stringResource(R.string.texto_grande), tamanoTexto == "grande") { onTamanoTextoCambio("grande") }
            }
        }
        Spacer(Modifier.height(12.dp))

        // ---------- BOTÓN GUARDAR ----------
        // Ya no hay scope.launch aquí: la pantalla solo avisa con onGuardar
        // y el ViewModel se encarga de la corrutina.
        Button(
            onClick = onGuardar,
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

        // Volver SIN guardar. Al volver a entrar, cargar() repone lo guardado.
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

// Texto grande y en negrita para los títulos de cada tarjeta.
@Composable
fun Titulo(texto: String) {
    Text(
        text = texto,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
    )
}

// Fila con un texto a la izquierda y un interruptor a la derecha.
// Esto es para el modo oscuro.
@Composable
fun FilaSwitch(texto: String, activado: Boolean, onCambio: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            // Ocupa todo el espacio libre y empuja el Switch a la derecha.
            modifier = Modifier.weight(1f)
        )
        Switch(checked = activado, onCheckedChange = onCambio)
    }
}

// Casilla con su texto. Se usa para elegir el tamaño del texto.
@Composable
fun FilaCheckbox(texto: String, marcado: Boolean, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = marcado, onCheckedChange = { onClick() })
        Text(text = texto, style = MaterialTheme.typography.bodyMedium)
    }
}

// Menú desplegable genérico.
@Composable
fun Desplegable(
    claves: List<String>,       // lo que se guarda: "es", "en"
    textos: List<String>,       // lo que se ve: "Español", "English"
    seleccionada: String,
    onSeleccion: (String) -> Unit
) {
    // Si la lista está abierta o no. Es un detalle visual que al
    // ViewModel no le importa, por eso se queda aquí con remember.
    var abierto by remember { mutableStateOf(false) }
    val posicion = claves.indexOf(seleccionada)

    // Box para que el menú aparezca justo debajo del botón.
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

// La preview usa ConfiguracionContenido con valores de ejemplo, porque
// la vista previa de Android Studio no puede crear un AndroidViewModel.
// Esta es la gran ventaja de separar la parte visual.
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
            ConfiguracionContenido(
                modoOscuro = false,
                idioma = "es",
                tamanoTexto = "mediano",
                onModoOscuroCambio = {},
                onIdiomaCambio = {},
                onTamanoTextoCambio = {},
                onGuardar = {},
                onVolver = {}
            )
        }
    }
}