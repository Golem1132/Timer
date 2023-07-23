package com.example.timer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.timer.screens.editexercise.EditExerciseScreen
import com.example.timer.screens.trainingcomposer.TrainingComposerScreen
import com.example.timer.screens.timer.TimerScreen
import com.example.timer.service.TimerService

@Composable
fun TimerNavigation(service: TimerService.MyBinder?) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = TimerRoutes.TimerScreen.route) {
        composable(route = TimerRoutes.TimerScreen.route) {
            TimerScreen(navController = navController, timerServiceBinder = service)
        }
        composable(route = TimerRoutes.TrainingComposer.route) {
            TrainingComposerScreen(navController)
        }
    }
}