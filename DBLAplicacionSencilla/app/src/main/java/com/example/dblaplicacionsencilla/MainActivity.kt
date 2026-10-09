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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
private data class Contacto(val titulo: Int, val fecha: String)

private val juegosSaga = listOf(
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

    @Preview
    @Composable
    fun ComponentesCarrusel() {
        LazyRow {
            items(NUMERO_TARJETAS_CARRUSEL) { index ->
                ElevatedCard(
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(width = 250.dp, height = 250.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = stringResource(R.string.Indicador) + ": $index",
                            style = TextStyle(fontSize = 20.sp),
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
                                text = stringResource(juegosSaga[index].titulo),
                                textAlign = TextAlign.Center,
                                style = TextStyle(fontSize = 20.sp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = juegosSaga[index].fecha,
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
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ComponentesCarrusel()
            MyButtons()
        }
    }

    @Composable
    fun MyButtons() {
        Spacer(
            modifier = Modifier.height(10.dp)
        )

        //Variables para los botones
        var habilitado1 by remember { mutableStateOf(true) }
        var habilitado2 by remember { mutableStateOf(false) }

        if (habilitado1) {
            Text (text = (stringResource(R.string.Aviso)), style = TextStyle(fontSize = 25.sp))
        } else {
            Text(("Daniel Balastegui López"), style = TextStyle(fontSize = 20.sp))
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Row() {
            Button(onClick = { habilitado1 = false; habilitado2 = true; }, enabled = habilitado1, shape = RoundedCornerShape(8.dp)) {
                if (!habilitado1) {
                    Text(text = (stringResource(R.string.Resetear)), style = TextStyle(fontSize = 20.sp))
                } else {
                    Text(text = (stringResource(R.string.Pulsar)), style = TextStyle(fontSize = 20.sp))
                }
            }
            Spacer(
                modifier = Modifier.width(10.dp)
            )
            Button(onClick = { habilitado2 = false; habilitado1 = true }, enabled = habilitado2, shape = RoundedCornerShape(8.dp)) {
                if (!habilitado2) {
                    Text(text = (stringResource(R.string.Resetear)), style = TextStyle(fontSize = 20.sp))
                } else {
                    Text(text = (stringResource(R.string.Pulsar)), style = TextStyle(fontSize = 20.sp))
                }
            }
        }
    }

@Preview(name = "Modo claro", showBackground = true)
@Preview(
    name = "Modo oscuro",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun VistaPreviaAplicacion() {
    DBLAplicacionSencillaTheme {
        AplicacionContenido(modifier = Modifier)
    }
}
