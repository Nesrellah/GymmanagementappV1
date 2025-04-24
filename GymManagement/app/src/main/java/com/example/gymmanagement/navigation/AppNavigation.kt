package com.example.gymmanagement.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gymmanagement.ui.screens.splash.SplashScreen
import com.example.gymmanagement.ui.screens.login.LoginScreen
import com.example.gymmanagement.ui.screens.register.RegisterScreen
import com.example.gymmanagement.ui.screens.admin.AdminScreen
import com.example.gymmanagement.ui.screens.member.MemberScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = AppRoutes.SPLASH
    ) {
        // Auth Routes
        composable(AppRoutes.SPLASH) {
            SplashScreen(navController)
        }
        
        composable(AppRoutes.LOGIN) {
            LoginScreen(
                navController = navController,
                onLoginSuccess = { isAdmin ->
                    val route = if (isAdmin) AppRoutes.ADMIN_EVENT else AppRoutes.MEMBER_WORKOUT
                    navController.navigate(route) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        
        composable(AppRoutes.REGISTER) {
            RegisterScreen(navController)
        }
        
        // Admin Routes - All routes point to AdminScreen which handles internal navigation
        composable(AppRoutes.ADMIN_EVENT) {
            AdminScreen()
        }
        
        composable(AppRoutes.ADMIN_WORKOUT) {
            AdminScreen()
        }
        
        composable(AppRoutes.ADMIN_PROGRESS) {
            AdminScreen()
        }
        
        // Member Routes - All routes point to MemberScreen which handles internal navigation
        composable(AppRoutes.MEMBER_WORKOUT) {
            MemberScreen()
        }
        
        composable(AppRoutes.MEMBER_EVENT) {
            MemberScreen()
        }
        
        composable(AppRoutes.MEMBER_PROGRESS) {
            MemberScreen()
        }
        
        composable(AppRoutes.MEMBER_PROFILE) {
            MemberScreen()
        }
    }
} 