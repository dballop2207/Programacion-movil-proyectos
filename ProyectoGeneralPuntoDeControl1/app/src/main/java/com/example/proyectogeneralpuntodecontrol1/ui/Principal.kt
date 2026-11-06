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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme
import com.example.proyectogeneralpuntodecontrol1.R

// PANTALLA PRINCIPAL (MENÚ)
// Muestra el logo y un menú hecho con tarjetas (ElevatedCard). Cada tarjeta
// es pulsable gracias a su parámetro onClick y lleva a una pantalla.
// - La pantalla NO sabe navegar: recibe funciones (onAcercaDe, onSalir...)
//   y las llama al pulsar. Quien decide qué hacen es Navegacion.kt, que es
//   quien tiene el navController. Así la pantalla es reutilizable y se
//   puede previsualizar sin navegación.
// - OpcionMenu es una función pequeña para no repetir el mismo código de
//   tarjeta cuatro veces: recibe el texto y la acción.
// - DIÁLOGO DE SALIR: mostrarDialogo es un estado (true/false). Al pulsar
//   "Salir" se pone a true y Compose dibuja el AlertDialog. El diálogo tiene:
//     · onDismissRequest: qué hacer si se toca fuera o se pulsa atrás (cerrarlo).
//     · confirmButton: llama a onSalir, que cierra la app.
//     · dismissButton: lo cierra poniendo mostrarDialogo a false.
//   Este estado se queda aquí con remember porque es un detalle visual que
//   solo le importa a esta pantalla.
// - Todos los textos salen de strings.xml, así que se traducen solos, y los
//   colores y estilos son los del tema (MaterialTheme).

@Composable
fun Principal(
    onAcercaDe: () -> Unit,
    onSobreNosotros: () -> Unit,
    onConfiguracion: () -> Unit,
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
        Spacer(Modifier.height(16.dp))

        OpcionMenu(stringResource(R.string.acerca_de), onAcercaDe)
        OpcionMenu(stringResource(R.string.ayuda), onSobreNosotros)
        OpcionMenu(stringResource(R.string.boton_configuracion), onConfiguracion)
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
            modifier = Modifier.padding(20.dp)
        )
    }
}

@Preview(name = "Tema claro", showBackground = true)
@Preview(name = "Tema oscuro", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PrincipalPreview() {
    AppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Principal(onAcercaDe = {}, onSobreNosotros = {}, onConfiguracion = {}, onSalir = {})
        }
    }
}