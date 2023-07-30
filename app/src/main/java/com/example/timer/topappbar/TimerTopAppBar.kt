package com.example.timer.topappbar

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TimerTopAppBar(
    navIcon: @Composable () -> Unit = {},
    title: @Composable () -> Unit = {},
    actions: @Composable () -> Unit = {}
) {
    Surface {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box {
                navIcon()
            }
            Box(
                modifier = Modifier.weight(1f)
                .padding(horizontal = 10.dp),
                contentAlignment = Alignment.Center,
                propagateMinConstraints = true
            ) {
                title()
            }
            Box {
                actions()
            }
        }
    }
}