// PANTALLA PRINCIPAL (MENÚ): logo, estado de la sesión y tarjetas que llevan a cada pantalla.
// No sabe navegar ni sabe de Firebase: recibe datos (conSesion, correo) y funciones (onAlgo) desde Navegacion.
// El Text muestra el correo si hay sesión; el TextButton cambia entre "Iniciar sesión" y "Cerrar sesión" según conSesion.
// mostrarDialogo controla el AlertDialog de salir (estado visual, por eso va con remember aquí).
package com.example.proyectogeneralpuntodecontrol1.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme
import com.example.proyectogeneralpuntodecontrol1.R

@Composable
fun Principal(
    conSesion: Boolean,
    correo: String?,
    onIniciarSesion: () -> Unit,
    onCerrarSesion: () -> Unit,
    onAcercaDe: () -> Unit,
    onSobreNosotros: () -> Unit,
    onConfiguracion: () -> Unit,
    onPokedex: () -> Unit,
    onSalir: () -> Unit
) {
    var mostrarDialogo by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_company),
            contentDescription = stringResource(R.string.nombre_logo),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            modifier = Modifier.size(160.dp)
        )
        Text(
            text = stringResource(R.string.nombre_logo),
            style = MaterialTheme.typography.displaySmall
        )
        Text(
            text = stringResource(R.string.desc_logo),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = if (conSesion) stringResource(R.string.sesion_de, correo ?: "") else stringResource(R.string.sin_sesion),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
        TextButton(onClick = if (conSesion) onCerrarSesion else onIniciarSesion) {
            Text(if (conSesion) stringResource(R.string.cerrar_sesion) else stringResource(R.string.iniciar_sesion))
        }
        Spacer(Modifier.height(8.dp))

        OpcionMenu(stringResource(R.string.acerca_de), onAcercaDe)
        OpcionMenu(stringResource(R.string.ayuda), onSobreNosotros)
        OpcionMenu(stringResource(R.string.boton_configuracion), onConfiguracion)
        OpcionMenu(stringResource(R.string.pokedex_titulo), onPokedex)
        OpcionMenu(stringResource(R.string.salir)) { mostrarDialogo = true }
    }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text(stringResource(R.string.dialogo_salir_titulo)) },
            text = { Text(stringResource(R.string.dialogo_salir_texto)) },
            confirmButton = {
                TextButton(onClick = onSalir) {
                    Text(stringResource(R.string.confirmar_salir))
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text(stringResource(R.string.cancelar))
                }
            }
        )
    }
}

@Composable
fun OpcionMenu(texto: String, onClick: () -> Unit) {
    ElevatedCard(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        )
    }
}

@Preview(name = "Tema claro", showBackground = true)
@Preview(name = "Tema oscuro", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PrincipalPreview() {
    AppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Principal(
                conSesion = false, correo = null, onIniciarSesion = {}, onCerrarSesion = {},
                onAcercaDe = {}, onSobreNosotros = {}, onConfiguracion = {}, onPokedex = {}, onSalir = {}
            )
        }
    }
}