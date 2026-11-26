package com.example.miscompras.ui

import android.content.res.Configuration
import com.example.miscompras.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.miscompras.ui.theme.MisComprasTheme

data class Producto(
    val id: Int,
    val nombre: String,
    val cantidad: Int,
    val comprado: Boolean = false,
)

@Composable
fun aplicacionTarea(modifier: Modifier) {
    val productosIniciales = listOf(
        Producto(id = 1, nombre = stringResource(R.string.Leche), cantidad = 3),
        Producto(id = 2, nombre = stringResource(R.string.Pan), cantidad = 1),
        Producto(id = 3, nombre = stringResource(R.string.Manzanas), cantidad = 5),
        Producto(id = 4, nombre = stringResource(R.string.Huevos), cantidad = 12)
    )

    val listaProductos = remember { productosIniciales.toMutableStateList() }

    val cambioAlMarcar = { index: Int, nuevoValor: Boolean ->
        listaProductos[index] = listaProductos[index].copy(comprado = nuevoValor)
    }

    listaProductos(
        listaProductos = listaProductos,
        cambioAlMarcar = cambioAlMarcar
    )
}

@Composable
fun listaProductos(listaProductos: List<Producto>, cambioAlMarcar:(Int, Boolean) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier.height(60.dp)
        )
        Text(
            text =  stringResource(R.string.MisCompras),
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(
            modifier = Modifier.height(16.dp)
        )
        LazyColumn{
            itemsIndexed (
                items=listaProductos,
                key={ _, producto -> producto.id
                } ) { index, producto ->
                ProductoCarta(
                    nombre = producto.nombre,
                    cantidad = producto.cantidad,
                    completada = producto.comprado,
                    cambioAlMarcar = { nuevoValor -> cambioAlMarcar(index, nuevoValor) }
                )
                Spacer(
                    modifier = Modifier.height(16.dp)
                )
            }
        }
    }
}

@Composable
fun ProductoCarta(nombre: String, cantidad: Int, completada: Boolean, cambioAlMarcar: (Boolean) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { cambioAlMarcar(!completada) },
        colors = CardDefaults.cardColors(
            containerColor = if (completada) MaterialTheme.colorScheme.primaryFixed
            else MaterialTheme.colorScheme.secondaryFixed,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = completada,
                onCheckedChange = { nuevoValor -> cambioAlMarcar(nuevoValor) }
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = nombre,
                    fontSize = 24.sp,
                    style = MaterialTheme.typography.labelMedium.copy(
                        textDecoration = if (completada) TextDecoration.LineThrough else TextDecoration.None,
                    )
                )
                Text(
                    text = cantidad.toString(),
                    fontSize = 17.sp,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        textDecoration = if (completada) TextDecoration.LineThrough else TextDecoration.None,
                    )
                )
            }
            Spacer(
                modifier = Modifier.height(10.dp)
            )
        }
    }
}

@Preview(
    name = "Modo blanco en Español",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    locale = "es"
)
@Preview(
    name = "Modo blanco en Ingles",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    locale = "en"
)
@Preview(
    name = "Modo oscuro en Español",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    locale = "es"
)
@Preview(
    name = "Modo oscuro en Ingles",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    locale = "en"
)
@Composable
fun GreetingPreview() {
    MisComprasTheme() {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            aplicacionTarea(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}