package com.example.cafearoma

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cafearoma.ui.theme.CafeAromaTheme

@Composable
fun SobreNosotros(onVolver: () -> Unit) {
    val context = LocalContext.current
    BackHandler(onBack = onVolver) // botón "atrás" del móvil

    // Los datos de contacto también salen de strings.xml
    val web = stringResource(R.string.contacto_web)
    val telefono = stringResource(R.string.contacto_telefono)
    val email = stringResource(R.string.contacto_email)
    val direccion = stringResource(R.string.contacto_direccion)
    val asuntoEmail = stringResource(R.string.email_asunto)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.sobre_titulo),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(12.dp))

        Image(
            painter = painterResource(R.drawable.img_cafeteria),
            contentDescription = stringResource(R.string.desc_cafeteria),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(MaterialTheme.shapes.large)
        )
        Spacer(Modifier.height(16.dp))

        Text(stringResource(R.string.sobre_historia), style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(16.dp))

        Card(shape = MaterialTheme.shapes.medium, modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text(stringResource(R.string.sobre_horario_titulo), style = MaterialTheme.typography.titleLarge)
                Text(stringResource(R.string.sobre_horario), style = MaterialTheme.typography.bodyLarge)
            }
        }
        Spacer(Modifier.height(16.dp))

        Text(stringResource(R.string.sobre_contacto_titulo), style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))

        // ---- Intents implícitos ----
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { abrirIntent(context, Intent(Intent.ACTION_VIEW, Uri.parse(web))) },
                modifier = Modifier.weight(1f)
            ) { Text(stringResource(R.string.accion_web)) }

            Button(
                onClick = { abrirIntent(context, Intent(Intent.ACTION_DIAL, Uri.parse("tel:$telefono"))) },
                modifier = Modifier.weight(1f)
            ) { Text(stringResource(R.string.accion_llamar)) }
        }
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:")).apply {
                        putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                        putExtra(Intent.EXTRA_SUBJECT, asuntoEmail)
                    }
                    abrirIntent(context, intent)
                },
                modifier = Modifier.weight(1f)
            ) { Text(stringResource(R.string.accion_email)) }

            Button(
                onClick = {
                    abrirIntent(context, Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + Uri.encode(direccion))))
                },
                modifier = Modifier.weight(1f)
            ) { Text(stringResource(R.string.accion_mapa)) }
        }
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, context.getString(R.string.compartir_texto, web))
                }
                abrirIntent(context, Intent.createChooser(intent, context.getString(R.string.accion_compartir)))
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text(stringResource(R.string.accion_compartir)) }

        Spacer(Modifier.height(24.dp))
        OutlinedButton(onClick = onVolver, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.volver))
        }
    }
}

// Lanza el intent y avisa si no hay ninguna app que pueda abrirlo
private fun abrirIntent(context: Context, intent: Intent) {
    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, context.getString(R.string.error_sin_app), Toast.LENGTH_SHORT).show()
    }
}

@Preview(showBackground = true)
@Composable
fun SobreNosotrosPreview() {
    CafeAromaTheme { SobreNosotros(onVolver = {}) }
}
