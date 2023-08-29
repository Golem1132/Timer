package com.example.timer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timer.R
import com.example.timer.extensions.nonScaled
import com.example.timer.model.Exercise
import com.example.timer.utils.secondsToTime

@Composable
fun ExerciseItem(item: Exercise) {


    Row(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = if (item.isRest)
                painterResource(id = R.drawable.rest_icon)
            else
                painterResource(id = R.drawable.work_icon),
            contentDescription = "Training type: ${
                if (item.isRest)
                    "Rest"
                else "Work"
            }"
        )

        Text(
            modifier = Modifier
                .weight(1f),
            text = item.name,
            fontWeight = FontWeight.Bold,
            overflow = TextOverflow.Ellipsis,
            softWrap = false,
            maxLines = 1,
            fontSize = 24.sp.nonScaled
        )
        Text(
            text = secondsToTime(item.duration),
            fontWeight = FontWeight.Bold,
            overflow = TextOverflow.Ellipsis,
            softWrap = false,
            maxLines = 1,
            fontSize = 24.sp.nonScaled
        )
    }
}

@Preview
@Composable
fun PrevExerciseItem() {
    ExerciseItem(
        item = Exercise(
            name = "",
            duration = 1000L,
            isRest = false,
            parentId = 0
        )
    )
}