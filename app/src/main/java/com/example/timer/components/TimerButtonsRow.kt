package com.example.timer.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.timer.R
import com.example.timer.internal.TimerState
import com.example.timer.service.TimerService

private const val buttonSize = 48

@Composable
fun TimerButtonsRow(timerServiceBinder: TimerService.MyBinder?) {
    val timerState = timerServiceBinder?.getCurrentState()?.collectAsState()
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            25.dp,
            Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (timerState?.value) {
            TimerState.PAUSED -> {
                Image(
                    modifier = Modifier
                        .size(buttonSize.dp)
                        .clip(CircleShape)
                        .clickable {
                            timerServiceBinder.stop()
                        },
                    painter = painterResource(id = R.drawable.stop_circle_24px),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                )
            }
            else -> Box {}
        }
        Image(
            modifier = Modifier
                .size((buttonSize * 1.75).dp)
                .clip(CircleShape)
                .clickable {
                    when (timerState?.value) {
                        TimerState.RUNNING -> timerServiceBinder.pause()
                        TimerState.PAUSED -> timerServiceBinder.resume()
                        else -> timerServiceBinder?.start()
                    }
                },
            painter = when (timerState?.value) {
                TimerState.RUNNING -> painterResource(id = R.drawable.pause_circle_24px)
                else -> painterResource(id = R.drawable.play_circle_24px)
            },
            contentDescription = "",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
        )
        Image(
            modifier = Modifier
                .size(buttonSize.dp)
                .clip(CircleShape)
                .clickable {
                    timerServiceBinder?.next()
                },
            painter = painterResource(id = R.drawable.skip_next_24px),
            contentDescription = "",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
        )
    }
}