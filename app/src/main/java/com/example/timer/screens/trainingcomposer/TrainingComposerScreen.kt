package com.example.timer.screens.trainingcomposer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.timer.durationpicker.DurationPicker
import com.example.timer.model.Exercise
import com.example.timer.topappbar.TimerTopAppBar

@Composable
fun TrainingComposerScreen(navController: NavController? = null) {
    val trainingTitle = remember {
        mutableStateOf("")
    }
    val showDialog = rememberSaveable {
        mutableStateOf(false)
    }
    val viewModel: TrainingComposerViewModel = viewModel()
    val exerciseList = viewModel.exercisesList.collectAsState()
    Scaffold(topBar = {
        TimerTopAppBar(
            navIcon = {
                Icon(imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Go back",
                    modifier = Modifier.clickable {
                        if (!showDialog.value)
                            navController?.popBackStack()
                        else
                            showDialog.value = false
                    })
            },
            title = {
                //TODO("Follow the trailing icon")
                if (!showDialog.value)
                    TextField(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        value = trainingTitle.value,
                        onValueChange = {
                            trainingTitle.value = it
                        },
                        label = {
                                Text(text = "Training name")
                        },
                        maxLines = 1,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        )
                    )
            },
            actions = {
                if (!showDialog.value)
                    Icon(imageVector = Icons.Default.Clear,
                        contentDescription = "Clear training",
                        modifier = Modifier.clickable {
                            viewModel.clearList()
                            trainingTitle.value = ""
                        })
            }
        )
    }, floatingActionButton = {
        if (!showDialog.value)
            FloatingActionButton(onClick = { showDialog.value = true }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add exercise")
            }
    },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(items = exerciseList.value) {
                Text(it.name)
            }
        }
        if (showDialog.value)
            DurationPicker(
                minutes = null,
                seconds = null,
                title = null,
                onCancel = {},
                onPositive = { duration, title ->
                    viewModel.addExercise(
                        Exercise(
                            name = title,
                            duration = duration,
                            parentId = 0
                        )
                    )
                    showDialog.value = false
                }
            )
    }

}

@Preview
@Composable
fun PreviewAddNewScreen() {
    TrainingComposerScreen()
}