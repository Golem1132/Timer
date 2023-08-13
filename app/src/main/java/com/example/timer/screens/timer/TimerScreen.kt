package com.example.timer.screens.timer

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.timer.components.TimerButtonsRow
import com.example.timer.components.TimerCircle
import com.example.timer.components.TimerExerciseInfo
import com.example.timer.navigation.TimerRoutes
import com.example.timer.service.TimerService
import com.example.timer.topappbar.TimerTopAppBar
import kotlinx.coroutines.launch


@Composable
fun TimerScreen(navController: NavController, timerServiceBinder: TimerService.MyBinder?) {
    val configuration = LocalConfiguration.current
    Scaffold(topBar = {
        TimerTopAppBar(
            navIcon = {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Open/Close drawer",
                    modifier = Modifier.clickable {
                        navController.navigate(TimerRoutes.HomeScreen.route)
                    }
                )
            },
            title = {
                Text(
                    text = "Training name",
                    textAlign = TextAlign.Center
                )
            }
        )
    }) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TimerCircle(timerServiceBinder, configuration.screenWidthDp)
                TimerExerciseInfo(timerServiceBinder)
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    TimerButtonsRow(timerServiceBinder)
                }
            }
        }
    }

}

