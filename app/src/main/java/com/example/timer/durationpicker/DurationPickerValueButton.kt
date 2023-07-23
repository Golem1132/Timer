package com.example.timer.durationpicker

import androidx.annotation.DrawableRes
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun DurationPickerValueButton(
    modifier: Modifier = Modifier,
    @DrawableRes img: Int,
    contentDescription: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier,
        onClick = onClick,
        shape = CircleShape,
        tonalElevation = 5.dp
    ) {
        Icon(
            painter = painterResource(id = img),
            contentDescription = contentDescription,
        )
    }
}