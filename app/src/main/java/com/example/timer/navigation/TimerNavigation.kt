package com.example.timer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.timer.screens.home.HomeScreen
import com.example.timer.screens.recordtrack.MapScreen
import com.example.timer.screens.rectypeselection.RecTypeSelectionScreen
import com.example.timer.screens.stopwatchscreen.StopWatchScreen
import com.example.timer.screens.timer.TimerScreen
import com.example.timer.screens.trainingcomposer.TrainingComposerScreen

@Composable
fun TimerNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = TimerRoutes.HomeScreen.route) {
        composable(route = TimerRoutes.TimerScreen.route) {
            TimerScreen(navController = navController)
        }
        composable(route = TimerRoutes.TrainingComposer.route) {
            TrainingComposerScreen(navController)
        }
        composable(route = TimerRoutes.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
        composable(route = TimerRoutes.RecordTrackScreen.route) {
            RecTypeSelectionScreen(navController = navController)
        }
        composable(route = TimerRoutes.MapScreen.route) {
            MapScreen(navController = navController)
        }
        composable(route = TimerRoutes.StopWatchScreen.route) {
            StopWatchScreen(navController = navController)
        }
    }
}