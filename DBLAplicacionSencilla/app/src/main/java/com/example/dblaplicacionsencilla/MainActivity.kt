package com.example.dblaplicacionsencilla

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.stringResource
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dblaplicacionsencilla.ui.theme.DBLAplicacionSencillaTheme

//Variable con el número de tarjetas que va a tener el carrusel
private const val NUMERO_TARJETAS_CARRUSEL = 5

//Clase para las tarjetas
private data class Contacto(val nombre: Int, val profesor: String)

private val asignatura = listOf(
    Contacto(R.string.Sistemas_informaticos, "Antonio Miguel"),
    Contacto(R.string.Acceso_a_datos, "Federico Huércano"),
    Contacto(R.string.Base_de_datos, "Francisco Jesús"),
    Contacto(R.string.Desarrollo_aplicaciones_movil, "Juan Manuel"),
    Contacto(R.string.Ingles, "Laura Pineda")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DBLAplicacionSencillaTheme() {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AplicacionContenido(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Composable
fun EstadoBotones() {
    var pulsado by rememberSaveable { mutableStateOf(false) }
    //Variables en un mutableStateOf para que las observe cuando haya cambios
    MyButtons (
        pulsado = pulsado,
        onPulsar = { pulsado = true },
        onResetear = { pulsado = false },
        modifier = Modifier
    )
}

@Preview
@Composable
fun ComponentesCarrusel() {
    // Creamos una lista grande sin elementos sin que afecte el rendimiento
    LazyRow {
        //Insertamos "items" que esto lo que hará será crear un número de objetos dentro de "items"
        // en este caso es una "ElevatedCard". Y le ponemos unas 5
        items(NUMERO_TARJETAS_CARRUSEL) { index ->
            ElevatedCard(
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .padding(8.dp)
                    .size(width = 250.dp, height = 250.dp)
            ) {
                //Metemos una caja y no una card porque el elemento
                //tarjeta es el único que no está centrado en la caja.
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = stringResource(R.string.Indicador) + " ${index + 1}",
                        style = TextStyle(fontSize = 15.sp),
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(asignatura[index].nombre),
                            textAlign = TextAlign.Center,
                            style = TextStyle(fontSize = 20.sp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = asignatura[index].profesor,
                            textAlign = TextAlign.Center,
                            style = TextStyle(fontSize = 18.sp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AplicacionContenido(modifier: Modifier) {
    //Esto proyecta todo el contenido de la aplicación en columnas y centrado
    //dentro de una función para ir preparandola para luego después ponerla en la función principal.
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ComponentesCarrusel()
        EstadoBotones()
    }
}

@Composable
fun MyButtons(pulsado: Boolean, onPulsar: () -> Unit, onResetear: () -> Unit, modifier: Modifier = Modifier) {
    Spacer(
        modifier = Modifier.height(10.dp)
    )

    if (!pulsado) {
        Text (text = (stringResource(R.string.Aviso)), style = TextStyle(fontSize = 22.sp))
    } else {
        Text(("Daniel Balastegui López"), style = TextStyle(fontSize = 22.sp))
    }

    Spacer(
        modifier = Modifier.height(10.dp)
    )

    Row() {

        Button(onClick = onPulsar,
            enabled = !pulsado,
            shape = RoundedCornerShape(8.dp))
        {
            if (pulsado) {
                Text(text = (stringResource(R.string.Resetear)), style = TextStyle(fontSize = 20.sp))
            } else {
                Text(text = (stringResource(R.string.Pulsar)), style = TextStyle(fontSize = 20.sp))
            }
        }
        Spacer(
            modifier = Modifier.width(10.dp)
        )

        Button(onClick = onResetear,
            enabled = pulsado,
            shape = RoundedCornerShape(8.dp))
        {
            if (!pulsado) {
                Text(text = (stringResource(R.string.Resetear)), style = TextStyle(fontSize = 20.sp))
            } else {
                Text(text = (stringResource(R.string.Pulsar)), style = TextStyle(fontSize = 20.sp))
            }
        }
    }
}

//Comprobar que el modo blanco y negro van bien en una aplicación
@Preview(
    name = "Modo claro",
    locale = "es",
    showBackground = true
)
@Preview(
    name = "Modo oscuro",
    showBackground = true,
    locale = "es",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun VistaPreviaAplicacion() {
    DBLAplicacionSencillaTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            AplicacionContenido(modifier = Modifier)
        }
    }
}
