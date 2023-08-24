package com.example.timer.screens.trainingcomposer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.timer.R
import com.example.timer.components.DurationPickerField
import com.example.timer.internal.TrainingComposerEvent
import com.example.timer.model.Exercise
import com.example.timer.topappbar.TimerTopAppBar

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PickDurationScreen(viewModel: TrainingComposerViewModel) {
    val keyboard = LocalSoftwareKeyboardController.current
    val minutesState = rememberSaveable {
        mutableStateOf((null ?: "00").toString())
    }
    val secondsState = rememberSaveable {
        mutableStateOf((null ?: "00").toString())
    }
    val titleState = rememberSaveable {
        mutableStateOf(null ?: "")
    }
    val textMeasurer =
        TextMeasurer(LocalFontFamilyResolver.current, LocalDensity.current, LayoutDirection.Ltr)
            .measure(
                text = "00",
                maxLines = 1,
                style = TextStyle.Default.copy(
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )

    /**
     * title [0],
     * duration [1]
     */
    val errorArrayState = remember {
        mutableStateOf(arrayOf(false, false))
    }
    val focusRequester = LocalFocusManager.current
    Scaffold(topBar = {
        TimerTopAppBar(
            navIcon = {
                Icon(imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Go back",
                    modifier = Modifier.clickable {
                        viewModel.sendUiEvent(TrainingComposerEvent.PickExercisesScreen)
                    })
            },
            allowShadow = false
        )
    }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(
                        10
                    ),
                    shadowElevation = 2.dp
                ) {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .horizontalScroll(rememberScrollState()),
                        value = titleState.value,
                        onValueChange = {
                            titleState.value =
                                if (it.length > 50)
                                    it.substring(0..49)
                                else
                                    it
                        },
                        label = {
                            if (titleState.value.isBlank())
                                Text("Exercise name")
                        },
                        supportingText = {
                            Text(text = "${titleState.value.length}/50")
                        },
                        isError = errorArrayState.value[0],
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            errorContainerColor = Color.Transparent
                        )
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    DurationPickerField(
                        modifier = Modifier
                            .width(textMeasurer.size.width.dp)
                            .onFocusChanged {
                                if (!it.hasFocus) {
                                    when (minutesState.value.length) {
                                        0 -> minutesState.value = "00"
                                        1 -> minutesState.value =
                                            "0" + minutesState.value
                                    }
                                } else {
                                    if (minutesState.value == "00")
                                        minutesState.value = ""
                                }
                            },
                        value = minutesState.value,
                        onValueChange = {
                            minutesState.value = if (it.length > 1)
                                it.substring(0..1)
                            else it
                        },
                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusRequester.moveFocus(FocusDirection.Right)
                            }
                        ),
                        imeAction = ImeAction.Next,
                        isError = errorArrayState.value[1]
                    )
                    Icon(
                        modifier = Modifier.height(IntrinsicSize.Min),
                        painter = painterResource(id = R.drawable.duration_divider),
                        contentDescription = "Divider"
                    )
                    DurationPickerField(
                        modifier = Modifier
                            .width(textMeasurer.size.width.dp)
                            .onFocusChanged {
                                if (!it.hasFocus) {
                                    if (secondsState.value.isNotBlank() && secondsState.value.toLong() > 59)
                                        secondsState.value = "59"
                                    when (secondsState.value.length) {
                                        0 -> secondsState.value = "00"
                                        1 -> secondsState.value =
                                            "0" + secondsState.value
                                    }
                                } else {
                                    if (secondsState.value == "00")
                                        secondsState.value = ""
                                }
                            },
                        value = secondsState.value,
                        onValueChange = {
                            secondsState.value = if (it.length > 1)
                                it.substring(0..1)
                            else it
                        },
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboard?.hide()
                                focusRequester.clearFocus(true)
                            }
                        ),
                        imeAction = ImeAction.Done,
                        isError = errorArrayState.value[1]
                    )

                }

            }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                    shape = RectangleShape,
                    onClick = {
                        val realMinutes = if (minutesState.value.isBlank())
                            0L
                        else
                            minutesState.value.toLong()

                        val realSeconds = if (secondsState.value.isBlank())
                            0L
                        else
                            secondsState.value.toLong()
                        val duration = ((realMinutes * 60) + realSeconds)

                        if (duration != 0L && titleState.value.isNotBlank()) {
                            viewModel.createdExercise = viewModel.createdExercise.copy(
                                duration = duration,
                                name = titleState.value
                            )
                            viewModel.addExercise()
                            viewModel.sendUiEvent(TrainingComposerEvent.PickExercisesScreen)
                        } else {
                            errorArrayState.value = arrayOf(
                                titleState.value.isBlank(),
                                duration <= 0
                            )
                        }
                    }) {
                    Text(text = "Save")
                }
            }
        }
    }
}