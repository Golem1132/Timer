package com.example.timer.screens.timer

import android.content.pm.ActivityInfo
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.timer.components.TimerButtonsRow
import com.example.timer.components.TimerCircle
import com.example.timer.components.TimerExerciseInfo
import com.example.timer.navigation.TimerRoutes
import com.example.timer.topappbar.TimerTopAppBar


@Composable
fun TimerScreen(navController: NavController) {
    val viewModel: TimerScreenViewModel = viewModel()
    val timerServiceBinder = viewModel.binder.collectAsState()
    val timerState = timerServiceBinder.value?.getCurrentState()?.collectAsState()
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
            if (configuration.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TimerCircle(timerServiceBinder.value, configuration.screenWidthDp)
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        TimerExerciseInfo(timerServiceBinder.value)
                        TimerButtonsRow(
                            timerState = timerState?.value,
                            onNext = {
                                timerServiceBinder.value?.nextExercise()
                            },
                            onPause = {
                                timerServiceBinder.value?.pauseExercise()
                            },
                            onResume = {
                                timerServiceBinder.value?.resumeExercise()
                            },
                            onStart = {
                                timerServiceBinder.value?.startExercise()
                            },
                            onStop = {
                                timerServiceBinder.value?.stopExercise()
                            }
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TimerCircle(timerServiceBinder.value, configuration.screenHeightDp)
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        TimerExerciseInfo(timerServiceBinder.value)
                        TimerButtonsRow(
                            timerState = timerState?.value,
                            onNext = {
                                timerServiceBinder.value?.nextExercise()
                            },
                            onPause = {
                                timerServiceBinder.value?.pauseExercise()
                            },
                            onResume = {
                                timerServiceBinder.value?.resumeExercise()
                            },
                            onStart = {
                                timerServiceBinder.value?.startExercise()
                            },
                            onStop = {
                                timerServiceBinder.value?.stopExercise()
                            }
                        )
                    }
                }
            }
        }
    }
}

