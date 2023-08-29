package com.example.timer.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TrainingItem(
    icon: ImageVector = Icons.Default.Favorite,
    title: String = "Training",
    time: String = "",
    color: Color = MaterialTheme.colorScheme.surface,
    onClick: () -> Unit = {}
) {

    Surface(
        modifier = Modifier
            .size(150.dp)
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(10),
        shadowElevation = 2.dp,
        color = color
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.weight(2f),
                imageVector = icon,
                contentDescription = "Training"
            )
            Text(
                modifier = Modifier.weight(1f),
                text = title
            )
            if (time.isNotBlank())
                Text(
                    modifier = Modifier.weight(1f),
                    text = "1:20:00"
                )
        }
    }
}

@Composable
fun TrainingItem(
    icon: Int,
    title: String = "Training",
    time: String = "",
    color: Color = MaterialTheme.colorScheme.surface,
    onClick: () -> Unit = {}
) {

    Surface(
        modifier = Modifier
            .size(150.dp)
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(10),
        shadowElevation = 2.dp,
        color = color
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.weight(2f),
                painter = painterResource(id = icon),
                contentDescription = "Training"
            )
            Text(
                modifier = Modifier.weight(1f),
                text = title
            )
            if (time.isNotBlank())
                Text(
                    modifier = Modifier.weight(1f),
                    text = "1:20:00"
                )
        }
    }
}


@Preview
@Composable
fun PreviewTrainingItem() {
    TrainingItem()
}