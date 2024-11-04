package net.estemon.studio.loccat

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import net.estemon.studio.loccat.screen.DistanceScreen
import net.estemon.studio.loccat.screen.EditPointScreen
import net.estemon.studio.loccat.screen.EditRouteScreen
import net.estemon.studio.loccat.screen.HintScreen
import net.estemon.studio.loccat.screen.LoginScreen
import net.estemon.studio.loccat.screen.MainScreen
import net.estemon.studio.loccat.screen.QRScannerScreen
import net.estemon.studio.loccat.screen.SplashScreen
import net.estemon.studio.loccat.screen.TeacherModeScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH_SCREEN
    ) {
        composable(Routes.SPLASH_SCREEN) {
            SplashScreen(navController = navController)
        }
        composable(Routes.MAIN_SCREEN) {
            MainScreen(navController = navController)
        }
        composable(Routes.QR_SCANNER_SCREEN) {
            QRScannerScreen(navController = navController)
        }
        composable(Routes.DISTANCE_SCREEN) {
            DistanceScreen(navController = navController)
        }
        composable(Routes.HINT_SCREEN) {
            HintScreen(navController = navController)
        }
        composable(Routes.LOGIN_SCREEN) {
            LoginScreen(navController = navController)
        }
        composable(Routes.TEACHER_MODE_SCREEN) {
            TeacherModeScreen(navController = navController)
        }
        composable(Routes.EDIT_POINT_SCREEN) {
            EditPointScreen(navController = navController)
        }
        composable(Routes.EDIT_ROUTE_SCREEN) {
            EditRouteScreen(navController = navController)
        }
    }
}

object Routes{
    const val SPLASH_SCREEN = "splash_screen"
    const val MAIN_SCREEN = "main_screen"
    const val QR_SCANNER_SCREEN = "qr_scanner_screen"
    const val DISTANCE_SCREEN = "distance_screen"
    const val HINT_SCREEN = "hint_screen"
    const val LOGIN_SCREEN = "login_screen"
    const val TEACHER_MODE_SCREEN = "teacher_mode_screen"
    const val EDIT_POINT_SCREEN = "edit_point_screen"
    const val EDIT_ROUTE_SCREEN = "edit_route_screen"
}