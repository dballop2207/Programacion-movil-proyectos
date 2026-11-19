// Rutas de todas las pantallas: menú, informativas, configuración, Pokédex y las dos de autenticación
package com.example.proyectogeneralpuntodecontrol1.model

sealed class Rutas(val ruta: String) {
    object Principal : Rutas("principal")
    object SobreNosotros : Rutas("sobre_nosotros")
    object AcercaDe : Rutas("acerca_de")
    object Configuracion : Rutas("configuracion")
    object Pokedex : Rutas("pokedex")
    object InicioSesion : Rutas("inicio_sesion")
    object Registro : Rutas("registro")
}