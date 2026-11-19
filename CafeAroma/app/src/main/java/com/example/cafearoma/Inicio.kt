package com.example.cafearoma

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cafearoma.ui.theme.CafeAromaTheme

// Pantallas posibles de la app
enum class Pantalla { INICIO, SOBRE_NOSOTROS, ACERCA_DE }

@Composable
fun PantallaPrincipal() {
    // rememberSaveable: mantiene la pantalla al girar el móvil
    var pantalla by rememberSaveable { mutableStateOf(Pantalla.INICIO) }

    when (pantalla) {
        Pantalla.INICIO -> Inicio(
            onSobreNosotros = { pantalla = Pantalla.SOBRE_NOSOTROS },
            onAcercaDe = { pantalla = Pantalla.ACERCA_DE }
        )
        Pantalla.SOBRE_NOSOTROS -> SobreNosotros(onVolver = { pantalla = Pantalla.INICIO })
        Pantalla.ACERCA_DE -> AcercaDe(onVolver = { pantalla = Pantalla.INICIO })
    }
}

@Composable
fun Inicio(onSobreNosotros: () -> Unit, onAcercaDe: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_logo),
            contentDescription = stringResource(R.string.desc_logo),
            modifier = Modifier.size(140.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(R.string.inicio_eslogan),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(32.dp))
        Button(
            onClick = onSobreNosotros,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.boton_sobre_nosotros))
        }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(
            onClick = onAcercaDe,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.boton_acerca_de))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InicioPreview() {
    CafeAromaTheme { Inicio({}, {}) }
}
