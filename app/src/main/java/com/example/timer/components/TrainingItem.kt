package com.example.timer.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TrainingItem() {

    Surface(
        modifier = Modifier
            .size(150.dp),
        shape = RoundedCornerShape(10),
        shadowElevation = 2.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.weight(2f),
                imageVector = Icons.Default.Favorite,
                contentDescription = "Training"
            )
            Text(
                modifier = Modifier.weight(1f),
                text = "Training name"
            )
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