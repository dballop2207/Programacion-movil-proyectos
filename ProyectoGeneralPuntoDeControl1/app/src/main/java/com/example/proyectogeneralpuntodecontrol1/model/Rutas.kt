package com.example.proyectogeneralpuntodecontrol1.model

// SEALED CLASS DE RUTAS (paquete modelo)
// Una sealed class es una clase "cerrada": solo puede tener los hijos
// declarados aquí dentro. Funciona como un enum, pero cada opción guarda
// un dato: el texto de su ruta.
// Cada object es una pantalla de la app, y su texto es la "dirección" que
// usa el NavController para saber a qué pantalla ir.
// Así escribimos Rutas.Configuracion.ruta en vez de "configuracion" a mano:
// si nos equivocamos, el compilador avisa.

sealed class Rutas(val ruta: String) {
    object Principal : Rutas("principal")
    object SobreNosotros : Rutas("sobre_nosotros")
    object AcercaDe : Rutas("acerca_de")
    object Configuracion : Rutas("configuracion")
}