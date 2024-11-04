package net.estemon.studio.loccat.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import net.estemon.studio.loccat.Routes

@Composable
fun EditRouteScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "EDIT ROUTE")
        Button(onClick = {
            navController.navigate(Routes.EDIT_POINT_SCREEN)
        }) {
            Text(text = "Edit Point")
        }
    }
}