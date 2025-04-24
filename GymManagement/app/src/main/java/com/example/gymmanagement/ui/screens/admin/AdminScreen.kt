package com.example.gymmanagement.ui.screens.admin

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gymmanagement.navigation.AppRoutes
import com.example.gymmanagement.ui.screens.admin.event.AdminEventScreen
import com.example.gymmanagement.ui.screens.admin.workout.AdminWorkoutScreen
import com.example.gymmanagement.ui.screens.admin.progress.AdminProgressScreen

@Composable
fun AdminScreen() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.ADMIN_EVENT
    ) {
        composable(AppRoutes.ADMIN_EVENT) {
            AdminEventScreen(
                onNavigateToWorkouts = { navController.navigate(AppRoutes.ADMIN_WORKOUT) },
                onNavigateToProgress = { navController.navigate(AppRoutes.ADMIN_PROGRESS) }
            )
        }

        composable(AppRoutes.ADMIN_WORKOUT) {
            AdminWorkoutScreen(
                onNavigateToEvents = { navController.navigate(AppRoutes.ADMIN_EVENT) },
                onNavigateToProgress = { navController.navigate(AppRoutes.ADMIN_PROGRESS) }
            )
        }

        composable(AppRoutes.ADMIN_PROGRESS) {
            AdminProgressScreen()
        }

        // TODO: Add other admin screens (Progress, Members) when they are created
    }
} 