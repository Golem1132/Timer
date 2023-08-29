package com.example.timer.screens.trainingcomposer

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
import com.example.timer.R
import com.example.timer.components.TrainingItem
import com.example.timer.internal.TrainingComposerEvent
import com.example.timer.topappbar.TimerTopAppBar
import com.example.timer.ui.theme.LightBlue
import com.example.timer.ui.theme.LightRed

@Composable
fun PickTypeScreen(viewModel: TrainingComposerViewModel) {
    Scaffold(topBar = {
        TimerTopAppBar(
            navIcon = {
                Icon(imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Go back",
                    modifier = Modifier.clickable {
                        viewModel.sendUiEvent(TrainingComposerEvent.PickExercisesScreen)
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
                    title = "Work",
                    color = LightBlue
                ) {
                    viewModel.createdExercise = viewModel.createdExercise.copy(isRest = false)
                    viewModel.sendUiEvent(TrainingComposerEvent.PickDurationScreen)
                }
                TrainingItem(
                    icon = R.drawable.rest_icon,
                    title = "Rest",
                    color = LightRed
                ) {
                    viewModel.createdExercise = viewModel.createdExercise.copy(isRest = true)
                    viewModel.sendUiEvent(TrainingComposerEvent.PickDurationScreen)
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
                    title = "Work",
                    color = LightBlue
                ) {
                    viewModel.createdExercise = viewModel.createdExercise.copy(isRest = false)
                    viewModel.sendUiEvent(TrainingComposerEvent.PickDurationScreen)
                }
                TrainingItem(
                    icon = R.drawable.rest_icon,
                    title = "Rest",
                    color = LightRed
                ) {
                    viewModel.createdExercise = viewModel.createdExercise.copy(isRest = true)
                    viewModel.sendUiEvent(TrainingComposerEvent.PickDurationScreen)
                }

            }
        }
    }

}