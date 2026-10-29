package com.example.proyectogeneralpuntodecontrol1

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme

//Esto son las 4 pantallas que hay en toda la aplicación, las usaremos para cambiarlas
enum class Pantalla { INICIO, SOBRE_NOSOTROS, ACERCA_DE, CONFIGURACION }

@Composable
fun PantallaPrincipal() {
    //Creamos un estado observable para mostar el contenido de la pantalla
    var pantalla by rememberSaveable { mutableStateOf(Pantalla.INICIO) }

    //Dependiendo de lo que pulsemos nos llevara a una pantalla u otra
    when(pantalla) {
        Pantalla.INICIO -> InicioPagina(
            onSobreNosotros = { pantalla = Pantalla.SOBRE_NOSOTROS},
            onAcercaDe = { pantalla = Pantalla.ACERCA_DE},
            onConfiguracion = { pantalla = Pantalla.CONFIGURACION }
        )
        Pantalla.SOBRE_NOSOTROS -> SobreNosotros(onVolver = { pantalla = Pantalla.INICIO })
        Pantalla.ACERCA_DE -> AcercaDe(onVolver = { pantalla = Pantalla.INICIO })
        Pantalla.CONFIGURACION -> Configuracion(onVolver = { pantalla = Pantalla.INICIO })
    }

}

@Composable
fun InicioPagina(
    onSobreNosotros: () -> Unit,
    onAcercaDe: () -> Unit,
    onConfiguracion: () -> Unit
) {
    //Lo organizamos con una columna para centrar todos los elementos.
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_company),
            contentDescription = "Logo de la compañía",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            modifier = Modifier.size(192.dp)
        )
        Text(
            text = stringResource(R.string.nombre_logo),
            style = MaterialTheme.typography.displaySmall
        )
        Text(
            text = stringResource(R.string.desc_logo),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(
            Modifier.height(8.dp)
        )

        //Si pulsamos este Botón nos lleva al apartado de Acerca De
        OutlinedButton(
            onClick = onAcercaDe,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.acerca_de)
            )
        }
        Spacer(Modifier.height(8.dp))

        //Si pulsamos este Botón nos lleva al apartado de sobre nosotros
        OutlinedButton(
            onClick = onSobreNosotros,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.ayuda)
            )
        }
        Spacer(Modifier.height(8.dp))

        //Si pulsamos este Botón nos lleva al apartado de configuración
        Button(
            onClick = onConfiguracion,
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.boton_configuracion)
            )
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
fun InicioPreview() {
    AppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            PantallaPrincipal()
        }
    }
}