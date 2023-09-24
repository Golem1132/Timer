package com.example.timer.screens.stopwatchscreen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.timer.components.TimerButtonsRow
import com.example.timer.extensions.nonScaled
import com.example.timer.service.TimerService
import com.example.timer.topappbar.TimerTopAppBar

@Composable
fun StopWatchScreen(navController: NavController) {

    val viewModel: StopWatchViewModel = viewModel()
    val binder = viewModel.binder.collectAsState()
    val timerState = binder.value?.getStopWatchState()?.collectAsState()
    val localContext = LocalContext.current
    val currentTime = binder.value?.getStopWatchCurrentTime()?.collectAsState()
    val totalTime = binder.value?.getStopWatchTotalTime()?.collectAsState()




    localContext.startForegroundService(Intent(localContext, TimerService::class.java))
    localContext.bindService(
        Intent(localContext, TimerService::class.java),
        viewModel.connection,
        Context.BIND_AUTO_CREATE
    )



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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = buildAnnotatedString {
                        val seconds = (totalTime?.value ?: 0) / 1000
                        val minutes = seconds / 60
                        val hours = minutes / 60
                        val realSeconds = with(seconds) {
                            if (this > 59L)
                                this - (minutes * 60L)
                            else
                                this
                        }
                        append(
                            "Total time: ${
                                if (hours < 10L)
                                    "0$hours"
                                else
                                    hours
                            }:${
                                if (minutes < 10L)
                                    "0$minutes"
                                else
                                    minutes
                            }:${
                                if (realSeconds < 10L)
                                    "0$realSeconds"
                                else
                                    realSeconds
                            }"
                        )
                    },
                    fontSize = 24.sp.nonScaled
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.8f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = buildAnnotatedString {
                            val seconds = (currentTime?.value ?: 0) / 1000
                            val minutes = seconds / 60
                            val hours = minutes / 60
                            val realSeconds = with(seconds) {
                                if (this > 59L)
                                    this - (minutes * 60L)
                                else
                                    this
                            }
                            append(
                                "${
                                    if (hours < 10L)
                                        "0$hours"
                                    else
                                        hours
                                }:${
                                    if (minutes < 10L)
                                        "0$minutes"
                                    else
                                        minutes
                                }:${
                                    if (realSeconds < 10L)
                                        "0$realSeconds"
                                    else
                                        realSeconds
                                }"
                            )
                        },
                        fontSize = 30.sp.nonScaled
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.2f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TimerButtonsRow(
                        timerState = timerState?.value,
                        onNext = {
                            binder.value?.saveRecordStopWatch()
                        },
                        onPause = {
                            binder.value?.pauseStopWatch()
                        },
                        onResume = {
                            binder.value?.startStopWatch()
                        },
                        onStart = {
                            binder.value?.startStopWatch()
                        },
                        onStop = {
                            binder.value?.resetStopWatch()
                        }
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun SD() {
    val navController = rememberNavController()

    StopWatchScreen(navController = navController)
}