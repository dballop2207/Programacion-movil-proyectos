package com.example.cafearoma.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val EsquemaClaro = lightColorScheme(
    primary = CafePrimarioClaro,
    onPrimary = CafeSobrePrimarioClaro,
    primaryContainer = CafeContenedorPrimarioClaro,
    secondary = CafeSecundarioClaro,
    onSecondary = CafeSobreSecundarioClaro,
    tertiary = CafeTerciarioClaro,
    background = CafeFondoClaro,
    onBackground = CafeSobreFondoClaro,
    surface = CafeSuperficieClaro,
    onSurface = CafeSobreSuperficieClaro
)

private val EsquemaOscuro = darkColorScheme(
    primary = CafePrimarioOscuro,
    onPrimary = CafeSobrePrimarioOscuro,
    primaryContainer = CafeContenedorPrimarioOscuro,
    secondary = CafeSecundarioOscuro,
    onSecondary = CafeSobreSecundarioOscuro,
    tertiary = CafeTerciarioOscuro,
    background = CafeFondoOscuro,
    onBackground = CafeSobreFondoOscuro,
    surface = CafeSuperficieOscuro,
    onSurface = CafeSobreSuperficieOscuro
)

@Composable
fun CafeAromaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Colores dinámicos (Android 12+). Lo dejamos en false para que
    // se vea siempre nuestra paleta personalizada.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> EsquemaOscuro
        else -> EsquemaClaro
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
