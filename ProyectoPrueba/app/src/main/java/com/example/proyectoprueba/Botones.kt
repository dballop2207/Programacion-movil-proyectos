package com.example.proyectoprueba

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun estadoBotones() {
    var pulsado by rememberSaveable { mutableStateOf(false) }

    Botones(
        pulsado = pulsado,
        onPulsar = { pulsado = true },
        onResetear = { pulsado = false },
    )
}
@Composable
fun Botones(pulsado: Boolean, onPulsar: () -> Unit, onResetear: () -> Unit, modifier: Modifier = Modifier) {
    Spacer(
        modifier.padding(4.dp)
    )
    if (!pulsado) {
        Text(
            text = stringResource(R.string.alerta),
            fontSize = 18.sp
        )
    } else {
        Text(
            text = "Daniel Balastegui López",
            fontSize = 18.sp
        )
    }
    Row(
    ) {
        Button(
            onClick = {onPulsar()}, enabled = !pulsado, shape = MaterialTheme.shapes.medium, modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = stringResource(R.string.pulsado)
            )
        }
        Button(
            onClick = {onResetear()}, enabled = pulsado, shape = MaterialTheme.shapes.medium, modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = stringResource(R.string.resetealo)
            )
        }
    }
}