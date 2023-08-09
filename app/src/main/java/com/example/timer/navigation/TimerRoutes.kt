package com.example.timer.navigation

sealed class TimerRoutes(val route: String) {
    object TimerScreen: TimerRoutes("TimerScreen")
    object TrainingComposer: TimerRoutes("TrainingComposer")
    object HomeScreen: TimerRoutes("HomeScreen")
    object JournalScreen: TimerRoutes("JournalScreen")
    object RecordTrackScreen: TimerRoutes("RecordTrackScreen")
    object SettingsScreen: TimerRoutes("SettingsScreen")
}
