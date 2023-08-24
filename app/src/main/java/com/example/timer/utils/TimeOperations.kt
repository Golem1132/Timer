package com.example.timer.utils

fun secondsToTime(seconds: Long): String {
    return "${
        with(seconds / 60) {
            if (this.toString().length == 1)
                "0$this"
            else this
        }
    } : ${
        with(seconds % 60) {
            if (this.toString().length == 1)
                "0$this"
            else this
        }
    }"
}