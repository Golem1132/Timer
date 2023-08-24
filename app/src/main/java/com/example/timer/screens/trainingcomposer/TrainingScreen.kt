package com.example.timer.screens.trainingcomposer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.timer.components.ExerciseItem
import com.example.timer.internal.TrainingComposerEvent
import com.example.timer.navigation.TimerRoutes
import com.example.timer.topappbar.TimerTopAppBar

@Composable
fun TrainingScreen(navController: NavController, viewModel: TrainingComposerViewModel) {
    val showDialog = rememberSaveable {
        mutableStateOf(false)
    }
    val exerciseList = viewModel.exercisesList.collectAsState()
    Scaffold(topBar = {
        TimerTopAppBar(
            navIcon = {
                Icon(imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Go back",
                    modifier = Modifier.clickable {
                        viewModel.sendUiEvent(TrainingComposerEvent.StartScreen)
                    })
            },
            actions = {
                Icon(imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Go back",
                    modifier = Modifier.clickable {
//                        SAVE MEEEE!!!
                        navController.navigate(TimerRoutes.HomeScreen.route)
                    })
            }
        )
    }, floatingActionButton = {
        FloatingActionButton(onClick = {
            viewModel.sendUiEvent(TrainingComposerEvent.PickTypeScreen)
        }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add exercise")
        }
    },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier.padding(
                    vertical = 20.dp,
                    horizontal = 5.dp
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Your exercises")
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(items = exerciseList.value) {
                    ExerciseItem(item = it)
                }
            }
        }
    }
}