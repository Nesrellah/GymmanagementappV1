package com.example.gymmanagement.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.SportsGymnastics
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gymmanagement.ui.screens.admin.AdminWorkoutScreen
import com.example.gymmanagement.viewmodel.AdminWorkoutViewModel

sealed class AdminScreen(val route: String, val title: String, val icon: ImageVector) {
    object Workouts : AdminScreen("workouts", "Workouts", Icons.Default.SportsGymnastics)
    object Events : AdminScreen("events", "Events", Icons.Default.Group)
    object Progress : AdminScreen("progress", "Progress", Icons.Default.ShowChart)
    object Members : AdminScreen("members", "Members", Icons.Default.Person)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminNavigation(
    workoutViewModel: AdminWorkoutViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val screens = listOf(
        AdminScreen.Workouts,
        AdminScreen.Events,
        AdminScreen.Progress,
        AdminScreen.Members
    )

    Scaffold(
        bottomBar = {
            AdminBottomNavigation(navController = navController, screens = screens)
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = AdminScreen.Workouts.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(AdminScreen.Workouts.route) {
                AdminWorkoutScreen(viewModel = workoutViewModel)
            }
            composable(AdminScreen.Events.route) {
                // TODO: Implement Events screen
            }
            composable(AdminScreen.Progress.route) {
                // TODO: Implement Progress screen
            }
            composable(AdminScreen.Members.route) {
                // TODO: Implement Members screen
            }
        }
    }
}

@Composable
fun AdminBottomNavigation(
    navController: NavHostController,
    screens: List<AdminScreen>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        screens.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
} 