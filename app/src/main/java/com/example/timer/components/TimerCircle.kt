package com.example.timer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import com.example.timer.service.TimerService

@Composable
fun TimerCircle(service: TimerService.MyBinder?) {
    val currentTime = service?.getCurrentTime()?.collectAsState()
    val currentExercise = service?.getCurrentExercise()?.collectAsState()
    Surface {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(color = Color.White),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.surface),
                    progress = getPercent(
                        currentTime?.value ?: 0L,
                        currentExercise?.value?.duration ?: 0L
                    ),
                    strokeWidth = 10.dp
                )
                Text(text = buildAnnotatedString {
                    val seconds = (currentTime?.value ?: 0) / 1000
                    val minutes = seconds / 60
                    val hours = minutes / 60
                    val realSeconds = with(seconds) {
                        if (this > 60L)
                            this - (minutes * 60L)
                        else
                            this
                    }
                    append("$hours:$minutes:$realSeconds")
                })
            }
        }
    }
}

private fun getPercent(current: Long, max: Long): Float {
    return if (current == 0L || max == 0L) 1f
    else {
        println(((current.toFloat() * 100) / max.toFloat()) / 100)
        ((current.toFloat() * 100) / max.toFloat()) / 100
    }

}