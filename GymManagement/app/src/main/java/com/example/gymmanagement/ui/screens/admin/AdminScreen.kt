package com.example.gymmanagement.ui.screens.admin

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gymmanagement.data.database.AppDatabase
import com.example.gymmanagement.navigation.AppRoutes
import com.example.gymmanagement.viewmodel.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.example.gymmanagement.ui.screens.admin.workout.AdminWorkoutScreen
import com.example.gymmanagement.ui.screens.admin.event.AdminEventScreen
import com.example.gymmanagement.ui.screens.admin.member.AdminMemberScreen
import com.example.gymmanagement.ui.screens.admin.progress.AdminProgressScreen
import com.example.gymmanagement.data.repository.WorkoutRepositoryImpl
import com.example.gymmanagement.data.repository.EventRepository
import com.example.gymmanagement.data.repository.UserRepositoryImpl
import com.example.gymmanagement.data.repository.TraineeProgressRepositoryImpl
import com.example.gymmanagement.utils.ImagePicker

private val PrimaryBlue = Color(0xFF0000FF)
private val BackgroundGray = Color(0xFFF5F5F5)
private val CardBlue = Color(0xFFE6E9FD)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {
    val context = LocalContext.current
    val adminNavController = rememberNavController()
    val currentRoute = currentRoute(adminNavController)

    // Initialize database and repositories
    val db = AppDatabase.getDatabase(context)
    val workoutDao = remember { db.workoutDao() }
    val eventDao = remember { db.eventDao() }
    val userDao = remember { db.userDao() }
    val userProfileDao = remember { db.userProfileDao() }
    val traineeProgressDao = remember { db.traineeProgressDao() }
    
    val workoutRepository = remember { WorkoutRepositoryImpl(workoutDao) }
    val eventRepository = remember { EventRepository(eventDao) }
    val userRepository = remember { UserRepositoryImpl(userDao, userProfileDao, context) }
    val traineeProgressRepository = remember { TraineeProgressRepositoryImpl(traineeProgressDao) }
    val imagePicker = remember { ImagePicker(context) }

    // Initialize ViewModels
    val adminWorkoutViewModel = remember { 
        AdminWorkoutViewModel(workoutRepository, imagePicker)
    }
    val adminEventViewModel = remember {
        AdminEventViewModel(eventRepository)
    }
    val adminMemberViewModel = remember {
        AdminMemberViewModel(userRepository)
    }
    val adminProgressViewModel = remember {
        AdminProgressViewModel(traineeProgressRepository)
    }

    // Check login state
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    LaunchedEffect(isLoggedIn, currentUser) {
        if (!isLoggedIn || currentUser == null || currentUser?.role?.lowercase() != "admin") {
            Log.d("AdminScreen", "Not logged in or not an admin, navigating to login")
            navController.navigate(AppRoutes.LOGIN) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    // Set up initial route
    LaunchedEffect(Unit) {
        Log.d("AdminScreen", "Setting up initial route")
        adminNavController.navigate(AppRoutes.ADMIN_WORKOUT) {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = PrimaryBlue
            ) {
                bottomNavItems.forEach { item ->
                    val isSelected = currentRoute == item.route
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                tint = if (isSelected) PrimaryBlue else Color.Gray
                            )
                        },
                        label = {
                            Text(
                                text = item.label,
                                color = if (isSelected) PrimaryBlue else Color.Gray
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
                            selectedIconColor = PrimaryBlue,
                            selectedTextColor = PrimaryBlue,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = CardBlue
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
                AdminWorkoutScreen(viewModel = adminWorkoutViewModel)
            }
            composable(AppRoutes.ADMIN_EVENT) {
                AdminEventScreen(viewModel = adminEventViewModel)
            }
            composable(AppRoutes.ADMIN_PROGRESS) {
                AdminProgressScreen(viewModel = adminProgressViewModel)
            }
            composable(AppRoutes.ADMIN_MEMBER) {
                AdminMemberScreen(viewModel = adminMemberViewModel)
            }
        }
    }

    // Handle back press (logout functionality)
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
