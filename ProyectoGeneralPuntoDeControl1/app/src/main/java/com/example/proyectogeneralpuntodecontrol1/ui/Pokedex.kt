// PANTALLA POKÉDEX (paquete ui) — la "Vista" del MVVM
// Tiene tres funciones, cada una dentro de la anterior:
//   Pokedex  ->  PokedexList  ->  PokedexItem (una por Pokémon)
// 1. Pokedex: pide la lista al ViewModelPokemon y la escucha con
//    collectAsState (cuando llegan los datos, se redibuja sola).
//    - Mientras la lista está vacía (aún descargando) muestra un
//      CircularProgressIndicator (el círculo de carga).
//    - Cuando ya hay datos, llama a PokedexList(listaPokemons).
//    - weight(1f) hace que la lista ocupe todo el hueco libre y deje
//      sitio abajo para el botón Volver.
// 2. PokedexList: un LazyColumn. A diferencia de Column, solo dibuja los
//    elementos que se ven en pantalla, así una lista larga no va lenta.
//    items(lista) { } crea un PokedexItem por cada Pokémon.
// 3. PokedexItem: una tarjeta con una fila (Row):
//    - AsyncImage (de Coil) descarga la imagen de Internet sin bloquear la
//      pantalla. Mientras carga, o si falla, enseña el placeholder.
//      replace("http://", "https://"): Android bloquea las URL http
//      (no seguras), así que las pasamos a https.
//      weight(0.3f) = la imagen ocupa el 30 % del ancho de la fila.
//    - A la derecha, una Column con el 70 % restante: número, nombre,
//      tipos y altura/peso.
// Colores y estilos salen de MaterialTheme; los textos fijos, de strings.xml.
package com.example.proyectogeneralpuntodecontrol1.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.proyectogeneralpuntodecontrol1.R
import com.example.proyectogeneralpuntodecontrol1.model.pokedex.DatosPokemon
import com.example.proyectogeneralpuntodecontrol1.viewmodel.ViewModelPokemon

@Composable
fun Pokedex(
    onVolver: () -> Unit,
    viewModel: ViewModelPokemon = viewModel()
) {
    val listaPokemons by viewModel.pokemons.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.pokedex_titulo),
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(Modifier.height(12.dp))

        if (listaPokemons.isEmpty()) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            PokedexList(listaPokemons, Modifier.weight(1f))
        }

        Spacer(Modifier.height(12.dp))
        Button(
            onClick = onVolver,
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.volver))
        }
    }
}

@Composable
fun PokedexList(listaPokemons: List<DatosPokemon>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(listaPokemons) { pokemon ->
            PokedexItem(pokemon)
        }
    }
}

@Composable
fun PokedexItem(pokemon: DatosPokemon) {
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(pokemon.img.replace("http://", "https://"))
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.placeholder),
                error = painterResource(R.drawable.placeholder),
                contentDescription = pokemon.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .weight(0.3f)
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(0.7f)) {
                Text(
                    text = "#${pokemon.num}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = pokemon.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = pokemon.type.joinToString(", "),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = stringResource(R.string.pokemon_altura_peso, pokemon.height, pokemon.weight),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}