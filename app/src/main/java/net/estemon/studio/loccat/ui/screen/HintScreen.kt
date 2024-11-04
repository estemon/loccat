package net.estemon.studio.loccat.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import net.estemon.studio.loccat.Routes

@Composable
fun HintScreen(
    navController: NavHostController
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "HINT")
        Text(text = "You have arrived to right coordinates!!")
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Find next QR walking 20 meters at North.")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate(Routes.QR_SCANNER_SCREEN) }) {
            Text(text = "SCAN QR")
        }
    }
}