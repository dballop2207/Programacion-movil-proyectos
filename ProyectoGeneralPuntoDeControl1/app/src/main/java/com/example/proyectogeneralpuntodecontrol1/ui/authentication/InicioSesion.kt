// InicioSesion: dos campos (correo y contraseña) guardados con remember, y un botón que llama al ViewModel.
// estado se escucha con collectAsState: si está Cargando sale el círculo, si hay error sale el mensaje.
// LaunchedEffect(estado) vigila el estado: cuando pasa a ConSesion, avisa con onSesionIniciada para volver a Principal.
// PasswordVisualTransformation pinta puntitos en vez de la contraseña.
package com.example.proyectogeneralpuntodecontrol1.ui.authentication

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.proyectogeneralpuntodecontrol1.R
import com.example.proyectogeneralpuntodecontrol1.viewmodel.AuthViewModel
import com.example.proyectogeneralpuntodecontrol1.viewmodel.EstadoAuth

@Composable
fun InicioSesion(
    viewModel: AuthViewModel,
    onSesionIniciada: () -> Unit,
    onIrARegistro: () -> Unit
) {
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    val estado by viewModel.estado.collectAsState()

    LaunchedEffect(estado) {
        if (estado is EstadoAuth.ConSesion) onSesionIniciada()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.login_titulo), style = MaterialTheme.typography.displaySmall)
        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text(stringResource(R.string.correo)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text(stringResource(R.string.contrasena)) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        when (val e = estado) {
            is EstadoAuth.Cargando -> CircularProgressIndicator()
            is EstadoAuth.CamposVacios -> Text(stringResource(R.string.auth_campos_vacios), color = MaterialTheme.colorScheme.error)
            is EstadoAuth.Error -> Text(stringResource(R.string.auth_error, e.mensaje), color = MaterialTheme.colorScheme.error)
            else -> {}
        }
        Spacer(Modifier.height(16.dp))

        Button(onClick = { viewModel.iniciarSesion(correo, contrasena) }, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.entrar))
        }
        TextButton(onClick = onIrARegistro) {
            Text(stringResource(R.string.ir_registro))
        }
    }
}