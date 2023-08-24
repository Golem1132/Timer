package com.example.timer.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

val TextUnit.nonScaled
    @Composable
    get() = (this.value / LocalDensity.current.fontScale).sp