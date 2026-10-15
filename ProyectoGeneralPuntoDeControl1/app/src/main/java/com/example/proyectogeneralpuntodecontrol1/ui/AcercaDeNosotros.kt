package com.example.proyectogeneralpuntodecontrol1.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.proyectogeneralpuntodecontrol1.ui.theme.AppShapes

@Composable
fun acercaDeNosotros(modifier: Modifier = Modifier) {
    Button (
        onClick = {},
        shape = AppShapes.extraLarge
    ){ Text(
        text = "Hola Mundo",
        style = TextStyle(fontSize = 40.sp),
        modifier = modifier
    ) }
}