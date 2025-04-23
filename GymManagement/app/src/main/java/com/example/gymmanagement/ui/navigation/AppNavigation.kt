package com.example.gymmanagement.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gymmanagement.viewmodel.AdminWorkoutViewModel

object AppRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val ADMIN_HOME = "admin_home"
    const val MEMBER_HOME = "member_home"
}

@Composable
fun AppNavigation(
    workoutViewModel: AdminWorkoutViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.LOGIN
    ) {
        composable(AppRoutes.LOGIN) {
            // TODO: Implement LoginScreen
            // For now, let's navigate directly to admin home
            // This should be replaced with proper login logic
            navController.navigate(AppRoutes.ADMIN_HOME) {
                popUpTo(AppRoutes.LOGIN) { inclusive = true }
            }
        }
        
        composable(AppRoutes.REGISTER) {
            // TODO: Implement RegisterScreen
        }
        
        composable(AppRoutes.ADMIN_HOME) {
            AdminNavigation(workoutViewModel = workoutViewModel)
        }
        
        composable(AppRoutes.MEMBER_HOME) {
            // TODO: Implement MemberNavigation
        }
    }
} 