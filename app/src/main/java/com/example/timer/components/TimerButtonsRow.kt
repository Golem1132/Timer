package com.example.timer.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.timer.R
import com.example.timer.internal.TimerState

private const val BUTTON_SIZE = 48

@Composable
fun TimerButtonsRow(
    timerState: TimerState?,
    onStop: () -> Unit,
    onStart: () -> Unit,
    onResume: () -> Unit,
    onPause: () -> Unit,
    onNext: () -> Unit,
    shouldShowNext: Boolean = true
) {
    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.spacedBy(
            25.dp,
            Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (timerState) {
            TimerState.PAUSED -> {
                Image(
                    modifier = Modifier
                        .size(BUTTON_SIZE.dp)
                        .clip(CircleShape)
                        .clickable {
                            onStop()
                        },
                    painter = painterResource(id = R.drawable.stop_circle_24px),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                )
            }

            else -> Box(
                modifier = Modifier
                    .size(BUTTON_SIZE.dp)
            ) {}
        }
        Image(
            modifier = Modifier
                .size((BUTTON_SIZE * 1.75).dp)
                .clip(CircleShape)
                .clickable {
                    when (timerState) {
                        TimerState.RUNNING -> onPause()
                        TimerState.PAUSED -> onResume()
                        else -> onStart()
                    }
                },
            painter = when (timerState) {
                TimerState.RUNNING -> painterResource(id = R.drawable.pause_circle_24px)
                else -> painterResource(id = R.drawable.play_circle_24px)
            },
            contentDescription = "",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
        )
        if (shouldShowNext)
            Image(
                modifier = Modifier
                    .size(BUTTON_SIZE.dp)
                    .clip(CircleShape)
                    .clickable {
                        onNext()
                    },
                painter = painterResource(id = R.drawable.skip_next_24px),
                contentDescription = "",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
            )
        else
            Box(
                modifier = Modifier
                    .size(BUTTON_SIZE.dp)
            ) {}
    }
}