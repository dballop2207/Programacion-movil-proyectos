// AuthViewModel: habla con Firebase para iniciar sesión, registrarse y cerrar sesión.
// Guarda en "estado" cómo va el login (sin sesión, cargando, con sesión o error) y la pantalla lo escucha con collectAsState.
// addOnCompleteListener es el "avísame cuando termines" de Firebase: la petición va por Internet y no bloquea la pantalla.
// CamposVacios va aparte para que la pantalla ponga el mensaje desde strings.xml (el ViewModel no puede usar stringResource).
package com.example.proyectogeneralpuntodecontrol1.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _estado = MutableStateFlow<EstadoAuth>(EstadoAuth.SinSesion)
    val estado: StateFlow<EstadoAuth> = _estado

    val correoUsuario: String?
        get() = auth.currentUser?.email

    init {
        comprobarSesion()
    }

    fun comprobarSesion() {
        _estado.value = if (auth.currentUser != null) EstadoAuth.ConSesion else EstadoAuth.SinSesion
    }

    fun iniciarSesion(correo: String, contrasena: String) {
        if (correo.isBlank() || contrasena.isBlank()) {
            _estado.value = EstadoAuth.CamposVacios
            return
        }
        _estado.value = EstadoAuth.Cargando
        auth.signInWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener { tarea ->
                _estado.value = if (tarea.isSuccessful) EstadoAuth.ConSesion
                else EstadoAuth.Error(tarea.exception?.message ?: "Error")
            }
    }

    fun registrar(correo: String, contrasena: String) {
        if (correo.isBlank() || contrasena.isBlank()) {
            _estado.value = EstadoAuth.CamposVacios
            return
        }
        _estado.value = EstadoAuth.Cargando
        auth.createUserWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener { tarea ->
                _estado.value = if (tarea.isSuccessful) EstadoAuth.ConSesion
                else EstadoAuth.Error(tarea.exception?.message ?: "Error")
            }
    }

    fun cerrarSesion() {
        auth.signOut()
        _estado.value = EstadoAuth.SinSesion
    }
}

sealed class EstadoAuth {
    object SinSesion : EstadoAuth()
    object Cargando : EstadoAuth()
    object ConSesion : EstadoAuth()
    object CamposVacios : EstadoAuth()
    data class Error(val mensaje: String) : EstadoAuth()
}