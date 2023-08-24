package com.example.timer.screens.trainingcomposer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.timer.internal.TrainingComposerEvent

@Composable
fun TrainingComposerScreen(navController: NavController) {

    val viewModel: TrainingComposerViewModel = viewModel()
    val uiEvent = viewModel.uiEvent.collectAsState()

    when (uiEvent.value) {
        TrainingComposerEvent.StartScreen -> {
            SetupTrainingParamsScreen(navController, viewModel)
        }

        TrainingComposerEvent.PickDurationScreen -> {
            PickDurationScreen(viewModel = viewModel)
        }

        TrainingComposerEvent.PickTypeScreen -> {
            PickTypeScreen(viewModel = viewModel)
        }

        else -> {
            TrainingScreen(navController, viewModel)
        }
    }
}