package com.example.timer.screens.timer

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
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
    val state = DrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    Scaffold(topBar = {
        TimerTopAppBar(
            navIcon = {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Open/Close drawer",
                    modifier = Modifier.clickable {
                        scope.launch {
                            when (state.currentValue) {
                                DrawerValue.Open -> state.close()
                                DrawerValue.Closed -> state.open()
                            }
                        }
                    }
                )
            },
            title = {
                Text(
                    text = "Training name",
                    textAlign = TextAlign.Center
                )
            },
            actions = {
                Icon(imageVector = Icons.Default.Add,
                    contentDescription = "Add new training",
                    modifier = Modifier.clickable {
                        navController.navigate(TimerRoutes.TrainingComposer.route)
                    })
            }
        )
    }) {
        DismissibleNavigationDrawer(
            gesturesEnabled = false,
            drawerState = state,
            drawerContent = {
                ModalDrawerSheet() {
                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {}
                }
            }) {
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    TimerCircle(timerServiceBinder)
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
}

