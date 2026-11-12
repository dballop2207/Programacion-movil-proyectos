// INSTANCIA DE RETROFIT (paquete model.pokedex)
// Configura Retrofit una sola vez para toda la app.
// - "object": solo existe UNA instancia (patrón singleton). No hace falta
//   crear Retrofit cada vez que queremos descargar algo.
// - BASE_URL: la parte inicial de la URL. Tiene que acabar en "/".
//   La parte final ("pokemon") está en ClientePokemon.
// - Retrofit.Builder(): construye Retrofit con la URL base y un conversor.
// - GsonConverterFactory: convierte el texto JSON en objetos DatosPokemon.
// - create(ClientePokemon::class.java): Retrofit genera el código de la interfaz.
// - by lazy: no se crea hasta la primera vez que se usa api.
package com.example.proyectogeneralpuntodecontrol1.model.pokedex

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://6af5ef48f6270fa08d68e99d.mockapi.io/"

    val api: ClientePokemon by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ClientePokemon::class.java)
    }
}
