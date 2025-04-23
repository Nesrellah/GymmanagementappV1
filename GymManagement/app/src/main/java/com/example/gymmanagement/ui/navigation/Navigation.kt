package com.example.gymmanagement.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gymmanagement.ui.screens.splash.SplashScreen
import com.example.gymmanagement.ui.screens.login.LoginScreen
import com.example.gymmanagement.ui.screens.register.RegisterScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.SPLASH
    ) {
        composable(AppRoutes.SPLASH) { SplashScreen(navController) }
        composable(AppRoutes.LOGIN) { LoginScreen(navController) }
        composable(AppRoutes.REGISTER) { RegisterScreen(navController) }

        // You’ll add admin/member routes here later
    }
}