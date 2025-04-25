package com.example.gymmanagement.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gymmanagement.ui.screens.splash.SplashScreen
import com.example.gymmanagement.ui.screens.login.LoginScreen
import com.example.gymmanagement.ui.screens.register.RegisterScreen
import com.example.gymmanagement.ui.screens.admin.AdminScreen
import com.example.gymmanagement.ui.screens.member.MemberScreen
import com.example.gymmanagement.viewmodel.AuthViewModel
import com.example.gymmanagement.GymManagementApp

@Composable
fun AppNavigation(app: GymManagementApp) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModel.Factory(app.userRepository)
    )
    
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()

    // Check initial state
    LaunchedEffect(Unit) {
        authViewModel.checkLoginState()
    }

    // Handle navigation based on auth state
    LaunchedEffect(isLoggedIn, currentUser) {
        if (isLoggedIn && currentUser != null) {
            val isAdmin = currentUser?.role?.lowercase() == "admin"
            val route = if (isAdmin) AppRoutes.ADMIN_EVENT else AppRoutes.MEMBER_WORKOUT
            navController.navigate(route) {
                popUpTo(AppRoutes.SPLASH) { inclusive = true }
                launchSingleTop = true
            }
        }
    }
    
    NavHost(
        navController = navController,
        startDestination = AppRoutes.SPLASH
    ) {
        // Auth Routes
        composable(AppRoutes.SPLASH) {
            SplashScreen(
                navController = navController,
                viewModel = authViewModel
            )
        }
        
        composable(AppRoutes.LOGIN) {
            LoginScreen(
                navController = navController,
                viewModel = authViewModel,
                onLoginSuccess = { isAdmin ->
                    val route = if (isAdmin) AppRoutes.ADMIN_EVENT else AppRoutes.MEMBER_WORKOUT
                    navController.navigate(route) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        
        composable(AppRoutes.REGISTER) {
            RegisterScreen(
                navController = navController,
                viewModel = authViewModel
            )
        }
        
        // Admin Routes
        composable(AppRoutes.ADMIN_EVENT) {
            AdminScreen(
                navController = navController,
                viewModel = authViewModel
            )
        }
        
        composable(AppRoutes.ADMIN_WORKOUT) {
            AdminScreen(
                navController = navController,
                viewModel = authViewModel
            )
        }
        
        composable(AppRoutes.ADMIN_PROGRESS) {
            AdminScreen(
                navController = navController,
                viewModel = authViewModel
            )
        }
        
        // Single composable for Member section that handles internal navigation
        composable(route = AppRoutes.MEMBER_WORKOUT) {
            MemberScreen(
                navController = navController,
                viewModel = authViewModel
            )
        }
    }
} 