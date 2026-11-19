package com.example.proyectoprueba

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

const val NUMERO_CARRUSELES = 5

data class Contacto(val titulo: Int, val nombres: String)

val contactos = listOf(
    Contacto(R.string.prueba1,  "Test1"),
    Contacto(R.string.prueba2,  "Test2"),
    Contacto(R.string.prueba3,  "Test3"),
    Contacto(R.string.prueba4,  "Test4"),
    Contacto(R.string.prueba5,  "Test5")
)
@Composable
fun Carrusel() {
    LazyRow() {
        items(NUMERO_CARRUSELES) { i ->
            ElevatedCard(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.size(250.dp).padding(8.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = stringResource(R.string.indicador)+" ${i+1}",
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Start,
                        fontSize = 18.sp
                    )
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(contactos[i].titulo),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp
                        )
                        Text(
                            text = contactos[i].nombres,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp

                        )
                    }
                }
            }
        }
    }
}
