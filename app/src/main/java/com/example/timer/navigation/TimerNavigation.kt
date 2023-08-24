package com.example.timer.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.timer.screens.home.HomeScreen
import com.example.timer.screens.timer.MainViewModel
import com.example.timer.screens.timer.TimerScreen
import com.example.timer.screens.trainingcomposer.TrainingComposerScreen
import com.example.timer.service.TimerService

@Composable
fun TimerNavigation(service: TimerService.MyBinder?) {
    val navController = rememberNavController()
    val mainViewModel = viewModel(modelClass = MainViewModel::class.java)
    NavHost(navController = navController, startDestination = TimerRoutes.HomeScreen.route) {
        composable(route = TimerRoutes.TimerScreen.route) {
            TimerScreen(navController = navController, timerServiceBinder = service)
        }
        composable(route = TimerRoutes.TrainingComposer.route) {
            TrainingComposerScreen(navController)
        }
        composable(route = TimerRoutes.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
    }
}