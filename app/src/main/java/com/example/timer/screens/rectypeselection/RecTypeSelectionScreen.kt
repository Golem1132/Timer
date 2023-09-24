package com.example.timer.screens.rectypeselection

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.timer.R
import com.example.timer.components.TrainingItem
import com.example.timer.navigation.TimerRoutes
import com.example.timer.topappbar.TimerTopAppBar
import com.example.timer.ui.theme.LightBlue
import com.example.timer.ui.theme.LightRed

@Composable
fun RecTypeSelectionScreen(navController: NavController) {
    Scaffold(topBar = {
        TimerTopAppBar(
            navIcon = {
                Icon(imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Go back",
                    modifier = Modifier.clickable {
                        navController.popBackStack()
                    })
            }
        )
    }
    ) { paddingValues ->
        if (LocalContext.current.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TrainingItem(
                    icon = R.drawable.work_icon,
                    title = "Location tracker",
                    color = LightBlue
                ) {
                    navController.navigate(TimerRoutes.MapScreen.route)
                }
                TrainingItem(
                    icon = R.drawable.rest_icon,
                    title = "Stop watch",
                    color = LightRed
                ) {
                    navController.navigate(TimerRoutes.StopWatchScreen.route)
                }

            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TrainingItem(
                    icon = R.drawable.work_icon,
                    title = "Location tracker",
                    color = LightBlue
                ) {
                    navController.navigate(TimerRoutes.MapScreen.route)
                }
                TrainingItem(
                    icon = R.drawable.rest_icon,
                    title = "Stop watch",
                    color = LightRed
                ) {
                    navController.navigate(TimerRoutes.StopWatchScreen.route)
                }

            }
        }
    }
}