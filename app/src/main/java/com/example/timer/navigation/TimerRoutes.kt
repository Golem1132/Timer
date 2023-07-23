package com.example.timer.navigation

sealed class TimerRoutes(val route: String) {
    object TimerScreen: TimerRoutes("TimerScreen")
    object TrainingComposer: TimerRoutes("TrainingComposer")
}
