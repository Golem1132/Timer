package com.example.timer.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.timer.R
import com.example.timer.components.DrawerItem
import com.example.timer.components.TrainingItem
import com.example.timer.navigation.TimerRoutes
import com.example.timer.topappbar.TimerTopAppBar
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: HomeViewModel = viewModel()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val trainingsList = viewModel.training.collectAsState()

    Scaffold(
        topBar = {
            TimerTopAppBar(
                navIcon = {
                    Icon(
                        modifier = Modifier.clickable {
                            scope.launch {
                                if (drawerState.isClosed)
                                    drawerState.open()
                                else
                                    drawerState.close()
                            }
                        },
                        imageVector =
                        if (drawerState.isClosed)
                            Icons.Default.Menu
                        else
                            Icons.Default.Close,
                        contentDescription = "Drawer menu"
                    )
                }
            )
        }
    ) {
        DismissibleNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(modifier = Modifier.padding(it)) {
                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            DrawerItem(
                                title = "Add training",
                                icon = R.drawable.add_icon,
                                isSelected = true
                            ) {
                                navController.navigate(TimerRoutes.TrainingComposer.route)

                            }
                            DrawerItem(title = "Choose training", icon = R.drawable.all_trainings_icon) {
                                navController.navigate(TimerRoutes.HomeScreen.route)
                            }
                            DrawerItem(title = "Journal", icon = R.drawable.journal_icon) {
                                navController.navigate(TimerRoutes.JournalScreen.route)
                            }
                            DrawerItem(title = "Record track", icon = R.drawable.register_training_icon) {
                                navController.navigate(TimerRoutes.RecordTrackScreen.route)
                            }
                            DrawerItem(title = "Settings", icon = R.drawable.settings_icon) {
                                navController.navigate(TimerRoutes.SettingsScreen.route)
                            }
                        }

                    }
                }
            }) {
            if (trainingsList.value.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(it)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "Looks like you haven't created one yet.\nYou can:"
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TrainingItem(
                            title = "Create training",
                            icon = R.drawable.add_icon
                        ) {
                            navController.navigate(TimerRoutes.TrainingComposer.route)
                        }
                        TrainingItem(
                            title = "Register workout",
                            icon = R.drawable.register_training_icon
                        ) {
                            navController.navigate(TimerRoutes.RecordTrackScreen.route)
                        }
                    }
                }
            } else {
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(it),
                    columns = GridCells.FixedSize(150.dp),
                    horizontalArrangement = Arrangement.spacedBy(
                        10.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(trainingsList.value) {
                        TrainingItem()
                    }
                }
            }
        }

    }

}