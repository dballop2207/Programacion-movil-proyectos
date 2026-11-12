// INTERFAZ DEL CLIENTE (paquete model.pokedex)
// Describe QUÉ peticiones podemos hacer al servidor. No tiene código dentro:
// Retrofit lee las anotaciones y genera él solo la implementación.
// - @GET("pokemon"): hace una petición GET a la parte final de la URL.
//   Se junta con la URL base de RetrofitInstance:
//   https://6af5ef48f6270fa08d68e99d.mockapi.io/  +  pokemon
// - suspend: la descarga tarda, así que se llama desde una corrutina
//   (en el ViewModel) para no bloquear la pantalla.
// - Devuelve List<DatosPokemon>: Gson convierte el JSON en esa lista.
package com.example.proyectogeneralpuntodecontrol1.model.pokedex

import retrofit2.http.GET

interface ClientePokemon {
    @GET("pokemon")
    suspend fun obtenerPokemons(): List<DatosPokemon>
}
