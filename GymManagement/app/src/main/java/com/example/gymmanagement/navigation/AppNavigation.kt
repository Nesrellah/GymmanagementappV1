package com.example.gymmanagement.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gymmanagement.ui.screens.admin.AdminScreen
import com.example.gymmanagement.ui.screens.login.LoginScreen
import com.example.gymmanagement.ui.screens.member.MemberScreen
import com.example.gymmanagement.ui.screens.register.RegisterScreen
import com.example.gymmanagement.ui.screens.splash.SplashScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    AppNavHost(navController = navController)
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.SPLASH
    ) {
        composable(route = AppRoutes.SPLASH) {
            SplashScreen(navController = navController)
        }

        composable(route = AppRoutes.LOGIN) {
            LoginScreen(
                navController = navController,
                onLoginSuccess = { isAdmin ->
                    val route = if (isAdmin) AppRoutes.ADMIN_EVENT else AppRoutes.MEMBER_WORKOUT
                    navController.navigate(route) {
                        popUpTo(AppRoutes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(route = AppRoutes.REGISTER) {
            RegisterScreen(navController = navController)
        }

        // Admin Section
        composable(route = AppRoutes.ADMIN_EVENT) {
            AdminScreen()
        }

        // Member Section
        composable(route = AppRoutes.MEMBER_WORKOUT) {
            MemberScreen()
        }
    }
} 