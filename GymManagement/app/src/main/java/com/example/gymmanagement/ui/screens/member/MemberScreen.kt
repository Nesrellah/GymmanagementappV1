package com.example.gymmanagement.ui.screens.member

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gymmanagement.data.database.AppDatabase
import com.example.gymmanagement.data.repository.MemberWorkoutRepositoryImpl
import com.example.gymmanagement.data.repository.UserRepositoryImpl
import com.example.gymmanagement.data.repository.EventRepository
import com.example.gymmanagement.navigation.AppRoutes
import com.example.gymmanagement.ui.screens.member.workout.MemberWorkoutScreen
import com.example.gymmanagement.ui.screens.member.profile.MemberProfileScreen
import com.example.gymmanagement.ui.screens.member.event.MemberEventScreen
import com.example.gymmanagement.viewmodel.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {
    val context = LocalContext.current
    val memberNavController = rememberNavController()
    val currentRoute = currentRoute(memberNavController)

    // Initialize database and repositories
    val db = AppDatabase.getDatabase(context)
    val memberWorkoutDao = remember { db.memberWorkoutDao() }
    val userDao = remember { db.userDao() }
    val userProfileDao = remember { db.userProfileDao() }
    val eventDao = remember { db.eventDao() }

    val workoutRepository = remember { MemberWorkoutRepositoryImpl(memberWorkoutDao) }
    val userRepository = remember { UserRepositoryImpl(userDao, userProfileDao, context) }
    val eventRepository = remember { EventRepository(eventDao) }

    // Initialize ViewModels
    val memberWorkoutViewModel = remember { MemberWorkoutViewModel(workoutRepository) }
    val memberProfileViewModel = remember { MemberProfileViewModel(userRepository) }
    val memberEventViewModel = remember { MemberEventViewModel(eventRepository) }

    // Check login state
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    LaunchedEffect(isLoggedIn, currentUser) {
        if (!isLoggedIn || currentUser == null || currentUser?.role?.lowercase() != "member") {
            Log.d("MemberScreen", "Not logged in or not a member, navigating to login")
            navController.navigate(AppRoutes.LOGIN) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    // Set up initial route
    LaunchedEffect(Unit) {
        Log.d("MemberScreen", "Setting up initial route")
        memberNavController.navigate(AppRoutes.MEMBER_WORKOUT) {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = Color.Black
            ) {
                bottomNavItems.forEach { item ->
                    val isSelected = currentRoute == item.route
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label
                            )
                        },
                        label = {
                            Text(
                                text = item.label,
                                color = if (isSelected) Color(0xFF4CAF50) else Color.Gray
                            )
                        },
                        selected = isSelected,
                        onClick = {
                            if (currentRoute != item.route) {
                                memberNavController.navigate(item.route) {
                                    popUpTo(memberNavController.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF4CAF50),
                            unselectedIconColor = Color.Gray,
                            indicatorColor = Color.White
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = memberNavController,
            startDestination = AppRoutes.MEMBER_WORKOUT,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(AppRoutes.MEMBER_WORKOUT) {
                MemberWorkoutScreen(viewModel = memberWorkoutViewModel)
            }
            composable(AppRoutes.MEMBER_EVENT) {
                MemberEventScreen(viewModel = memberEventViewModel)
            }
            composable(AppRoutes.MEMBER_PROFILE) {
                currentUser?.let { user ->
                    MemberProfileScreen(
                        userEmail = user.email,
                        viewModel = memberProfileViewModel,
                        onLogout = {
                            viewModel.logout()
                            navController.navigate(AppRoutes.LOGIN) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                } ?: run {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Please log in to view your profile")
                    }
                }
            }
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
        label = "Daily Workout",
        icon = Icons.Default.Person,
        route = AppRoutes.MEMBER_WORKOUT
    ),
    BottomNavItem(
        label = "Gym Events",
        icon = Icons.Default.Person,
        route = AppRoutes.MEMBER_EVENT
    ),
    BottomNavItem(
        label = "Profile",
        icon = Icons.Default.Person,
        route = AppRoutes.MEMBER_PROFILE
    )
)