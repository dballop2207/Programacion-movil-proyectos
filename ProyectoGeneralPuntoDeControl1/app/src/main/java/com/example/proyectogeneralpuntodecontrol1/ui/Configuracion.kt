// PANTALLA DE CONFIGURACIÓN (vista del MVVM)
// La pantalla se divide en dos funciones:
//  1. Configuracion: conecta con el ViewModel. No guarda estado propio.
//  2. ConfiguracionContenido: solo dibuja (así funciona la Preview).
// - Recibe el navController (lo pide el enunciado) para poder volver a
//   Principal con popBackStack(), que quita esta pantalla de la pila.
// - viewModel() da el ViewModel: lo crea la primera vez y luego reutiliza el mismo.
// - LaunchedEffect(Unit) llama a cargar() una vez al entrar.
// - Al pulsar Guardar se llama a viewModel.guardar { hayCambios -> ... }:
//     · true  -> toast LARGO (Toast.LENGTH_LONG) y vuelve a Principal.
//     · false -> toast CORTO (Toast.LENGTH_SHORT) y se queda aquí.
// - Los textos de los toasts se leen arriba con stringResource porque es
//   @Composable y no se puede llamar dentro del onClick.
// - STATE HOISTING: Titulo, FilaSwitch, FilaCheckbox y Desplegable no
//   guardan nada; reciben el valor y avisan con una función cuando cambia.
package com.example.proyectogeneralpuntodecontrol1.ui

import android.content.res.Configuration
import android.widget.Toast
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
import androidx.navigation.NavHostController
import com.example.compose.AppTheme
import com.example.proyectogeneralpuntodecontrol1.R
import com.example.proyectogeneralpuntodecontrol1.viewmodel.ConfiguracionViewModel
import com.example.proyectogeneralpuntodecontrol1.viewmodel.ResultadoGuardado

@Composable
fun Configuracion(
    navController: NavHostController,
    viewModel: ConfiguracionViewModel = viewModel()
) {
    val context = LocalContext.current
    val mensajeGuardado = stringResource(R.string.config_guardado)
    val mensajeSinCambios = stringResource(R.string.config_sin_cambios)
    val mensajeNoValido = stringResource(R.string.config_combinacion_no_valida)

    LaunchedEffect(Unit) {
        viewModel.cargar()
    }

    ConfiguracionContenido(
        modoOscuro = viewModel.modoOscuro,
        idioma = viewModel.idioma,
        tamanoTexto = viewModel.tamanoTexto,
        onModoOscuroCambio = viewModel::cambiarModoOscuro,
        onIdiomaCambio = viewModel::cambiarIdioma,
        onTamanoTextoCambio = viewModel::cambiarTamanoTexto,
        onGuardar = {
            viewModel.guardar { resultado ->
                when (resultado) {
                    ResultadoGuardado.GUARDADO -> {
                        Toast.makeText(context, mensajeGuardado, Toast.LENGTH_LONG).show()
                        navController.popBackStack()
                    }
                    ResultadoGuardado.SIN_CAMBIOS ->
                        Toast.makeText(context, mensajeSinCambios, Toast.LENGTH_SHORT).show()
                    ResultadoGuardado.COMBINACION_NO_VALIDA ->
                        Toast.makeText(context, mensajeNoValido, Toast.LENGTH_SHORT).show()
                }
            }
        },
        onVolver = { navController.popBackStack() }
    )
}

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

        // SWITCH: modo oscuro
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
                    onSeleccion = onIdiomaCambio
                )
            }
        }
        Spacer(Modifier.height(12.dp))

        // TAMAÑO DEL TEXTO (selección única)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Titulo(stringResource(R.string.config_tamano_texto))
                FilaCheckbox(stringResource(R.string.texto_pequeno), tamanoTexto == "pequeno") { onTamanoTextoCambio("pequeno") }
                FilaCheckbox(stringResource(R.string.texto_mediano), tamanoTexto == "mediano") { onTamanoTextoCambio("mediano") }
                FilaCheckbox(stringResource(R.string.texto_grande), tamanoTexto == "grande") { onTamanoTextoCambio("grande") }
            }
        }
        Spacer(Modifier.height(12.dp))

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

        OutlinedButton(
            onClick = onVolver,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.volver))
        }
    }
}

@Composable
fun Titulo(texto: String) {
    Text(
        text = texto,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
    )
}

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
            modifier = Modifier.weight(1f)
        )
        Switch(checked = activado, onCheckedChange = onCambio)
    }
}

@Composable
fun FilaCheckbox(texto: String, marcado: Boolean, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = marcado, onCheckedChange = { onClick() })
        Text(text = texto, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun Desplegable(
    claves: List<String>,
    textos: List<String>,
    seleccionada: String,
    onSeleccion: (String) -> Unit
) {
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

@Preview(name = "Tema claro", showBackground = true)
@Preview(name = "Tema oscuro", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
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
