package com.example.timer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timer.service.TimerService

@Composable
fun TimerExerciseInfo(service: TimerService.MyBinder?) {
    val currentExercise = service?.getCurrentExercise()?.collectAsState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(text = buildAnnotatedString {
            withStyle(SpanStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold)) {
                append("Round")
            }
        })
        Text(text = buildAnnotatedString {
            withStyle(SpanStyle(fontSize = 20.sp, fontWeight = FontWeight.Light)) {
                append(currentExercise?.value?.name ?: "")
            }
        })
    }
}