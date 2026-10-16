package com.example.proyectogeneralpuntodecontrol1.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectogeneralpuntodecontrol1.ui.theme.AppShapes
import com.example.proyectogeneralpuntodecontrol1.ui.theme.bodyFontFamily

@Composable
fun acercaDeNosotros(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
    ) {
        Button(
            onClick = {},
            shape = AppShapes.extraLarge
        ) {
            Text(
                text = "Hola Mundo",
                style = TextStyle(
                    fontFamily = bodyFontFamily,
                    fontSize = 25.sp
                )
            )
        }
        Spacer (
            modifier = Modifier.height(25.dp)
        )
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.TopStart
        ) {
            MyImage()
        }
    }



}