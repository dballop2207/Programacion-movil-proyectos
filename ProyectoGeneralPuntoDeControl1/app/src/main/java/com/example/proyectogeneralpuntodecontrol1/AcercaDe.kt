package com.example.proyectogeneralpuntodecontrol1

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// onVolver es la acción que se ejecuta para volver a inicio.
// Esta pantalla no sabe CÓMO se vuelve; solo la llama.
// Quien decide qué hace es PantallaPrincipal, que es quien la pasa.
@Composable
fun AcercaDe(onVolver: () -> Unit) {
    // El gesto/botón "atrás" del móvil también ejecuta onVolver,
    // así vuelve a inicio en vez de cerrar la app.
    BackHandler(onBack = onVolver)

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //Insertamos una imagen usando Image() y dentro
        //ponemos painter = painterResource(id = R.drawable.image-que-buscamos).
        Image(
            painter = painterResource(id = R.drawable.logo_company),
            contentDescription = "Logo de la compañía",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            modifier = Modifier.size(128.dp)
        )
        Text(
            text = stringResource(R.string.nombre_logo),
            style = MaterialTheme.typography.displaySmall
        )
        Text(
            text = stringResource(R.string.desc_acerca_de),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(16.dp))

        //Carta para poner los datos
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.autor),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Daniel Balastegui López",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Proyecto general: punto de control I",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        Spacer(Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.licencia_titulo),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.licencia_descripcion),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.derechos),
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            // Al pulsar "Go Back" se ejecuta onVolver, que en PantallaPrincipal
            // pone pantalla = Pantalla.INICIO y se muestra la pantalla de inicio.
            onClick = onVolver,
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                stringResource(R.string.volver)
            )
        }
    }
}