package com.example.gymmanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gymmanagement.ui.theme.GymManagementAppTheme
import com.example.gymmanagement.ui.screens.login.LoginScreen
import com.example.gymmanagement.ui.screens.splash.SplashScreen
import com.example.gymmanagement.ui.screens.register.RegisterScreen
import com.example.gymmanagement.ui.screens.admin.workout.AdminWorkoutScreen

// MainActivity.kt
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GymManagementAppTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "splash") {
                    composable("splash") { SplashScreen(navController) }
                    composable("login") { LoginScreen(navController) }
                    composable("register") { RegisterScreen(navController) }
                    composable("admin_workout") { 
                        AdminWorkoutScreen(
                            onNavigateToEvents = { navController.navigate("admin_events") },
                            onNavigateToProgress = { navController.navigate("admin_progress") },
                            onNavigateToMembers = { navController.navigate("admin_members") }
                        )
                    }
                    // Add other screens
                }
            }
        }
    }
}


