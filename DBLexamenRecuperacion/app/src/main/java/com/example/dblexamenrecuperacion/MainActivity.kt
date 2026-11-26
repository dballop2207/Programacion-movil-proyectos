package com.example.dblexamenrecuperacion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dblexamenrecuperacion.ui.aplicacionTarea
import com.example.dblexamenrecuperacion.ui.theme.DBLexamenRecuperacionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DBLexamenRecuperacionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                    aplicacionTarea(modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DBLexamenRecuperacionTheme {
        aplicacionTarea(modifier = Modifier.padding(8.dp))
    }
}