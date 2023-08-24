package com.example.timer.screens.trainingcomposer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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

@Preview
@Composable
fun PreviewComposerScreen() {
    val navController = rememberNavController()
    TrainingComposerScreen(navController = navController)
}