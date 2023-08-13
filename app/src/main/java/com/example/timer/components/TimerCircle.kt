package com.example.timer.components

import androidx.annotation.FloatRange
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.timer.service.TimerService

@Composable
fun TimerCircle(service: TimerService.MyBinder?, size: Int) {
    val currentTime = service?.getCurrentTime()?.collectAsState()
    val currentExercise = service?.getCurrentExercise()?.collectAsState()
    val density = LocalDensity.current
    val textLayoutResult =
        TextMeasurer(LocalFontFamilyResolver.current, density, LayoutDirection.Ltr)
            .measure(buildAnnotatedString {
                val seconds = (currentTime?.value ?: 0) / 1000
                val minutes = seconds / 60
                val hours = minutes / 60
                val realSeconds = with(seconds) {
                    if (this > 59L)
                        this - (minutes * 60L)
                    else
                        this
                }
                append(
                    "${
                        if (hours < 10L)
                            "0$hours"
                        else
                            hours
                    }:${
                        if (minutes < 10L)
                            "0$minutes"
                        else
                            minutes
                    }:${
                        if (realSeconds < 10L)
                            "0$realSeconds"
                        else
                            realSeconds
                    }"
                )
            })
    Surface {
        Canvas(
            modifier = Modifier
                .size(size.dp)
                .padding(10.dp)
        ) {
            drawArc(
                color = Color.Red, startAngle = 180f, sweepAngle = 180f, useCenter = false,
                style = Stroke(width = 20f, cap = StrokeCap.Round)
            )
            drawText(
                textLayoutResult = textLayoutResult,
                topLeft = getCenter(textLayoutResult, density.density, size)
            )
            drawArc(
                color = Color.Green,
                startAngle = 180f,
                sweepAngle = getPercent(
                    currentTime?.value ?: 0L,
                    currentExercise?.value?.duration ?: 0L
                ),
                useCenter = false,
                style = Stroke(width = 20f, cap = StrokeCap.Round)
            )
        }
    }
}

private fun getCenter(textMeasurements: TextLayoutResult, density: Float, windowSize: Int): Offset {
    val textWidthCenter = ((textMeasurements.size.width / 2))
    val textHeightCenter = ((textMeasurements.size.height / 2))
    val padding = 10 * density
    val widthCenter = (windowSize / 2) * density
    val heightCenter = ((windowSize / 4) * density)

    return Offset(
        widthCenter - textWidthCenter - padding,
        heightCenter - textHeightCenter - padding
    )
}

private fun getPercent(current: Long, max: Long): Float {
    return if (current == 0L || max == 0L) 0f
    else {
        180f - ((180f * current) / max)
    }

}