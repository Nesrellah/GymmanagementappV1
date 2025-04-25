package com.example.gymmanagement.ui.screens.admin

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymmanagement.ui.screens.admin.workout.AdminWorkoutScreen
import com.example.gymmanagement.ui.screens.admin.event.AdminEventScreen
import com.example.gymmanagement.ui.screens.admin.member.AdminMemberScreen
import com.example.gymmanagement.ui.screens.admin.progress.AdminProgressScreen
import androidx.navigation.NavController
import com.example.gymmanagement.navigation.AppRoutes
import com.example.gymmanagement.viewmodel.AuthViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.gymmanagement.data.database.AppDatabase

@Composable
fun AdminScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {
    val context = LocalContext.current
    val adminNavController = rememberNavController()
    val currentRoute = currentRoute(adminNavController)

    // Observe login state
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    // If not logged in, navigate to the login screen
    LaunchedEffect(isLoggedIn, currentUser) {
        if (!isLoggedIn || currentUser == null || currentUser?.role?.lowercase() != "admin") {
            navController.navigate(AppRoutes.LOGIN) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    // Initially navigate to the Admin's default screen (Admin Workout)
    LaunchedEffect(Unit) {
        adminNavController.navigate(AppRoutes.ADMIN_WORKOUT) {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = Color(0xFF0000CD)
            ) {
                bottomNavItems.forEach { item ->
                    val isSelected = currentRoute == item.route
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                tint = if (isSelected) Color(0xFF00FF00) else Color.Gray
                            )
                        },
                        label = {
                            Text(
                                text = item.label,
                                color = if (isSelected) Color(0xFF00FF00) else Color.Gray
                            )
                        },
                        selected = isSelected,
                        onClick = {
                            if (currentRoute != item.route) {
                                adminNavController.navigate(item.route) {
                                    popUpTo(adminNavController.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF00FF00),
                            selectedTextColor = Color(0xFF00FF00),
                            indicatorColor = Color.White
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = adminNavController,
            startDestination = AppRoutes.ADMIN_WORKOUT,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(AppRoutes.ADMIN_WORKOUT) {
                AdminWorkoutScreen(
                    onNavigateToEvents = {
                        adminNavController.navigate(AppRoutes.ADMIN_EVENT)
                    },
                    onNavigateToProgress = {
                        adminNavController.navigate(AppRoutes.ADMIN_PROGRESS)
                    }
                )
            }
            composable(AppRoutes.ADMIN_EVENT) {
                AdminEventScreen(
                    onNavigateToWorkouts = {
                        adminNavController.navigate(AppRoutes.ADMIN_WORKOUT)
                    },
                    onNavigateToProgress = {
                        adminNavController.navigate(AppRoutes.ADMIN_PROGRESS)
                    }
                )
            }
            composable(AppRoutes.ADMIN_MEMBER) {
                AdminMemberScreen()
            }
            composable(AppRoutes.ADMIN_PROGRESS) {
                AdminProgressScreen()
            }
        }
    }

    // Handle the back press (logout functionality)
    BackHandler {
        viewModel.logout()
        navController.navigate(AppRoutes.LOGIN) {
            popUpTo(0) { inclusive = true }
        }
    }
}

@Composable
private fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

private data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

private val bottomNavItems = listOf(
    BottomNavItem(
        label = "Workouts",
        icon = Icons.Default.Person,
        route = AppRoutes.ADMIN_WORKOUT
    ),
    BottomNavItem(
        label = "Events",
        icon = Icons.Default.Person,
        route = AppRoutes.ADMIN_EVENT
    ),
    BottomNavItem(
        label = "Members",
        icon = Icons.Default.Person,
        route = AppRoutes.ADMIN_MEMBER
    ),
    BottomNavItem(
        label = "Progress",
        icon = Icons.Default.Person,
        route = AppRoutes.ADMIN_PROGRESS
    )
)
