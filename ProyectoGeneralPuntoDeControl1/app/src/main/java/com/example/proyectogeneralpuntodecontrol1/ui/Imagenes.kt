package com.example.proyectogeneralpuntodecontrol1.ui

import com.example.proyectogeneralpuntodecontrol1.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun MyImage() {
    Image(
        painter = painterResource(id = R.drawable.cara_feliz),
        contentDescription = "My image",
        modifier = Modifier.size(128.dp)
    )
}
