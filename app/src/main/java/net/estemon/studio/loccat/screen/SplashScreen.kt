package net.estemon.studio.loccat.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import net.estemon.studio.loccat.Routes

@Composable
fun SplashScreen(navController: NavHostController) {
    Text(text = "SPLASH")
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate(Routes.MAIN_SCREEN) {
            popUpTo(Routes.SPLASH_SCREEN) { inclusive = true }
        }
    }
}