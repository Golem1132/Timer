package com.example.timer.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun DrawerItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean = false,
    action: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shadowElevation = 2.dp,
        shape = RoundedCornerShape(0.dp, 20.dp, 20.dp, 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .clickable {
                    action()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = title
            )
            Icon(
                imageVector = icon,
                contentDescription = "Category icon"
            )
        }
    }
}

@Composable
fun DrawerItem(
    title: String,
    @DrawableRes icon: Int,
    isSelected: Boolean = false,
    action: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        tonalElevation = if (isSelected)
            10.dp
        else 0.dp,
        shadowElevation = 2.dp,
        shape = RoundedCornerShape(0.dp, 20.dp, 20.dp, 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .clickable {
                    action()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = title
            )
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "Category icon"
            )
        }
    }
}
