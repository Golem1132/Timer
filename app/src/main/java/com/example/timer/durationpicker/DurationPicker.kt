package com.example.timer.durationpicker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.timer.R
import com.example.timer.components.DurationPickerField
import com.example.timer.topappbar.TimerTopAppBar

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DurationPicker(
    minutes: Long?,
    seconds: Long?,
    title: String?,
    onCancel: () -> Unit,
    onPositive: (Long, String) -> Unit
) {

    val keyboard = LocalSoftwareKeyboardController.current
    val minutesState = rememberSaveable {
        mutableStateOf((minutes ?: "00").toString())
    }
    val secondsState = rememberSaveable {
        mutableStateOf((seconds ?: "00").toString())
    }
    val titleState = rememberSaveable {
        mutableStateOf(title ?: "")
    }
    val focusRequester = LocalFocusManager.current
    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(topBar = {
            TimerTopAppBar(
                navIcon = {
                    Icon(
                        modifier = Modifier.clickable {
                          onCancel()
                        },
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close edit window"
                    )
                }
            )
        }) {
            Column(
                modifier = Modifier.padding(it),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = titleState.value,
                    onValueChange = {
                        titleState.value =
                            if (it.length > 50)
                                it.substring(0..49)
                            else
                                it
                    },
                    label = {
                        Text("Exercise name")
                    },
                    supportingText = {
                        Text(text = "${titleState.value.length}/50")
                    },
                    maxLines = 1,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent
                    )
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DurationPickerField(
                        modifier = Modifier
                            .weight(1f)
                            .onFocusChanged {
                                if (!it.hasFocus) {
                                    when (minutesState.value.length) {
                                        0 -> minutesState.value = "00"
                                        1 -> minutesState.value = "0" + minutesState.value
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
                        imeAction = ImeAction.Next
                    )
                    Icon(
                        modifier = Modifier.height(IntrinsicSize.Min),
                        painter = painterResource(id = R.drawable.duration_divider),
                        contentDescription = "Divider"
                    )
                    DurationPickerField(
                        modifier = Modifier
                            .weight(1f)
                            .onFocusChanged {
                                if (!it.hasFocus) {
                                    if (secondsState.value.isNotBlank() && secondsState.value.toLong() > 59)
                                        secondsState.value = "59"
                                    when (secondsState.value.length) {
                                        0 -> secondsState.value = "00"
                                        1 -> secondsState.value = "0" + secondsState.value
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
                        imeAction = ImeAction.Done
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(onClick = {
                        val realMinutes = if (minutesState.value.isBlank())
                            0L
                        else
                            minutesState.value.toLong()

                        val realSeconds = if (secondsState.value.isBlank())
                            0L
                        else
                            secondsState.value.toLong()
                        val duration = ((realMinutes * 60) + realSeconds)

                        if (duration != 0L && titleState.value.isNotBlank())
                            onPositive(duration, titleState.value)
                    }) {
                        Text(text = "Save")
                    }
                }
            }
        }
    }
}