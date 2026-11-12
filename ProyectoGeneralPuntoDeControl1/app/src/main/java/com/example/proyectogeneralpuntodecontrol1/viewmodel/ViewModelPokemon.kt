// VIEWMODEL DEL POKÉDEX (paquete viewmodel)
// Es la "V-M" de MVVM: pide los datos al repositorio y los guarda en un
// estado que la pantalla Pokedex.kt escucha.
//   Vista (Pokedex.kt) <-> ViewModelPokemon <-> Modelo (PokemonRepository)
// - repository: de donde saca la lista de Pokémon.
// - _pokemons (privado) y pokemons (público): el patrón "backing property".
//   El ViewModel puede CAMBIAR _pokemons (MutableStateFlow), pero la pantalla
//   solo puede LEER pokemons (StateFlow). Así nadie de fuera lo modifica.
// - emptyList(): empieza vacía. Mientras esté vacía, la pantalla muestra
//   el indicador circular de carga.
// - init { }: se ejecuta nada más crear el ViewModel, así la descarga
//   empieza en cuanto se abre la Pokédex.
// - viewModelScope.launch: corrutina para descargar sin bloquear la pantalla.
// - try/catch: si falla Internet, la app no se cierra; se escribe el error
//   en el Logcat de Android Studio.
package com.example.proyectogeneralpuntodecontrol1.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectogeneralpuntodecontrol1.model.pokedex.DatosPokemon
import com.example.proyectogeneralpuntodecontrol1.model.pokedex.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ViewModelPokemon : ViewModel() {

    private val repository = PokemonRepository()

    private val _pokemons = MutableStateFlow<List<DatosPokemon>>(emptyList())
    val pokemons: StateFlow<List<DatosPokemon>> = _pokemons

    init {
        cargarPokemons()
    }

    private fun cargarPokemons() {
        viewModelScope.launch {
            try {
                _pokemons.value = repository.obtenerPokemons()
            } catch (e: Exception) {
                Log.e("ViewModelPokemon", "Error al cargar los Pokémon", e)
            }
        }
    }
}