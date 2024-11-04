package net.estemon.studio.loccat

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
import androidx.navigation.compose.rememberNavController
import net.estemon.studio.loccat.ui.theme.LocCatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LocCatTheme {
                LocCat("")
            }
        }
    }
}

@Composable
fun LocCat(name: String, modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    AppNavHost(navController = navController)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LocCatTheme {
        LocCat("Android")
    }
}