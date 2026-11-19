package com.example.cafearoma

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cafearoma.ui.theme.CafeAromaTheme

@Composable
fun AcercaDe(onVolver: () -> Unit) {
    val context = LocalContext.current
    BackHandler(onBack = onVolver)
    val urlLicencia = stringResource(R.string.licencia_url)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Imagen que representa a la app
        Image(
            painter = painterResource(R.drawable.ic_logo),
            contentDescription = stringResource(R.string.desc_logo),
            modifier = Modifier.size(120.dp)
        )
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(R.string.acerca_version),
            style = MaterialTheme.typography.labelLarge
        )
        Spacer(Modifier.height(24.dp))

        // Autores
        Card(shape = MaterialTheme.shapes.medium, modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text(stringResource(R.string.acerca_autores_titulo), style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))
                Text(stringResource(R.string.acerca_autor_1), style = MaterialTheme.typography.bodyLarge)
                Text(stringResource(R.string.acerca_autor_2), style = MaterialTheme.typography.bodyLarge)
                HorizontalDivider(Modifier.padding(vertical = 12.dp))
                Text(stringResource(R.string.acerca_centro), style = MaterialTheme.typography.bodyLarge)
            }
        }
        Spacer(Modifier.height(16.dp))

        // Licencia
        Card(shape = MaterialTheme.shapes.medium, modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text(stringResource(R.string.acerca_licencia_titulo), style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))
                Text(stringResource(R.string.acerca_licencia), style = MaterialTheme.typography.bodyLarge)
                TextButton(onClick = {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(urlLicencia)))
                }) {
                    Text(stringResource(R.string.acerca_ver_licencia))
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.acerca_copyright),
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        OutlinedButton(onClick = onVolver, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.volver))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AcercaDePreview() {
    CafeAromaTheme { AcercaDe(onVolver = {}) }
}
