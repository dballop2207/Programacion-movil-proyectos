// REPOSITORIO DE POKÉMON (paquete model.pokedex)
// Es el intermediario entre el ViewModel y la fuente de los datos.
// El ViewModel no sabe si los Pokémon vienen de Internet, de una base de
// datos o de un archivo: solo le pide la lista al repositorio.
// Si mañana cambiamos de dónde salen los datos, solo cambia este archivo.
// - api: el cliente que nos da RetrofitInstance.
// - obtenerPokemons(): es suspend porque llama a Internet; devuelve la lista.
package com.example.proyectogeneralpuntodecontrol1.model.pokedex

class PokemonRepository {
    private val api = RetrofitInstance.api

    suspend fun obtenerPokemons(): List<DatosPokemon> = api.obtenerPokemons()
}
