// DATA CLASS DE UN POKÉMON (paquete model.pokedex)
// Representa UN Pokémon tal y como llega en el JSON de mockapi.io.
// - "data class" es una clase pensada solo para guardar datos: Kotlin le
//   crea automáticamente equals, toString, copy...
// - Los nombres de las propiedades tienen que ser IGUALES que las claves
//   del JSON ("num", "name", "img"...). Así Gson (el conversor de Retrofit)
//   sabe en qué propiedad meter cada valor sin configurar nada más.
// - id es String porque mockapi.io lo genera como texto ("1", "2"...).
// - type es una List<String> porque en el JSON es una lista: ["Grass", "Poison"].
package com.example.proyectogeneralpuntodecontrol1.model.pokedex

data class DatosPokemon(
    val id: String,
    val num: String,
    val name: String,
    val img: String,
    val type: List<String>,
    val height: String,
    val weight: String
)
