package com.example.cafearoma.ui.theme

import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Formas personalizadas: se usan con MaterialTheme.shapes.small / medium / large
val Shapes = Shapes(
    small = RoundedCornerShape(8.dp),      // botones, chips
    medium = RoundedCornerShape(16.dp),    // tarjetas
    large = CutCornerShape(topEnd = 24.dp) // imágenes destacadas
)
