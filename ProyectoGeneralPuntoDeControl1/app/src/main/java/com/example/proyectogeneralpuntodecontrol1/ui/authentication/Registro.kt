// Registro: igual que InicioSesion, pero el botón llama a viewModel.registrar (crea el usuario en Firebase).
// Firebase, al crear la cuenta, deja la sesión ya iniciada, así que el estado pasa a ConSesion y LaunchedEffect vuelve a Principal.
// Firebase exige una contraseña de al menos 6 caracteres; si no, devuelve un Error con el motivo (en inglés).
// onIrALogin y onVolver los decide Navegacion (los dos vuelven a la pantalla anterior).
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
fun Registro(
    viewModel: AuthViewModel,
    onSesionIniciada: () -> Unit,
    onIrALogin: () -> Unit,
    onVolver: () -> Unit
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
        Text(stringResource(R.string.registro_titulo), style = MaterialTheme.typography.displaySmall)
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

        Button(onClick = { viewModel.registrar(correo, contrasena) }, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.registrarse))
        }
        TextButton(onClick = onIrALogin) {
            Text(stringResource(R.string.ir_login))
        }

        Spacer(Modifier.height(8.dp))
        OutlinedButton(
            onClick = onVolver,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.volver))
        }
    }
}