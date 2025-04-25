package com.example.gymmanagement.navigation

import android.util.Log
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

    // Handle initial navigation based on login state
    LaunchedEffect(isLoggedIn, currentUser) {
        if (isLoggedIn && currentUser != null) {
            val route = if (currentUser!!.role.lowercase() == "admin") AppRoutes.ADMIN_WORKOUT else AppRoutes.MEMBER_WORKOUT
            Log.d("AppNavigation", "Navigating to route: $route for user: ${currentUser!!.email}")
            navController.navigate(route) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = AppRoutes.SPLASH
    ) {
        composable(AppRoutes.SPLASH) {
            SplashScreen(navController, authViewModel)
        }

        composable(AppRoutes.LOGIN) {
            LoginScreen(
                navController = navController,
                viewModel = authViewModel,
                onLoginSuccess = { isAdmin ->
                    val route = if (isAdmin) AppRoutes.ADMIN_WORKOUT else AppRoutes.MEMBER_WORKOUT
                    Log.d("AppNavigation", "Login success, navigating to route: $route")
                    navController.navigate(route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
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

        composable(AppRoutes.ADMIN_WORKOUT) {
            AdminScreen(
                navController = navController,
                viewModel = authViewModel
            )
        }

        composable(AppRoutes.MEMBER_WORKOUT) {
            MemberScreen(
                navController = navController,
                viewModel = authViewModel
            )
        }
    }
}
