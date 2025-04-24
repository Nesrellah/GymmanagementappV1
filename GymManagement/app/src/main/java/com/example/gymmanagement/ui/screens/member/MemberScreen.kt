package com.example.gymmanagement.ui.screens.member

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gymmanagement.data.database.AppDatabase
import com.example.gymmanagement.data.dao.MemberWorkoutDao
import com.example.gymmanagement.data.model.MemberWorkout
import com.example.gymmanagement.data.repository.MemberWorkoutRepositoryImpl
import com.example.gymmanagement.navigation.AppRoutes
import com.example.gymmanagement.ui.screens.member.workout.MemberWorkoutScreen
import com.example.gymmanagement.ui.screens.member.profile.MemberProfileScreen
import com.example.gymmanagement.viewmodel.MemberWorkoutViewModel
import com.example.gymmanagement.viewmodel.AuthViewModel
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberScreen() {
    val navController = rememberNavController()
    val currentRoute = currentRoute(navController)
    val context = LocalContext.current

    // Initialize DAO from Room database
    val memberWorkoutDao = remember { AppDatabase.getDatabase(context).memberWorkoutDao() }

    // Initialize repository with DAO
    val repository = remember { MemberWorkoutRepositoryImpl(memberWorkoutDao) }

    // Initialize ViewModel with repository
    val memberWorkoutViewModel = remember { MemberWorkoutViewModel(repository) }

    Scaffold(
        bottomBar = {
            MemberBottomNavigation(
                currentRoute = currentRoute,
                onNavigate = { route -> navController.navigate(route) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            NavHost(
                navController = navController,
                startDestination = AppRoutes.MEMBER_WORKOUT
            ) {
                composable(AppRoutes.MEMBER_WORKOUT) {
                    MemberWorkoutScreen(viewModel = memberWorkoutViewModel)
                }
                composable(AppRoutes.MEMBER_EVENT) {
                    // TODO: Add MemberEventScreen
                }
                composable(AppRoutes.MEMBER_PROFILE) {
                    val authViewModel: AuthViewModel = viewModel()
                    val currentUser by authViewModel.currentUser.collectAsState(initial = null)
                    
                    currentUser?.let { user ->
                        MemberProfileScreen(traineeId = user.id.toString())
                    } ?: run {
                        Text("No user logged in")
                    }
                }
            }
        }
    }
}

@Composable
fun MemberBottomNavigation(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
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
                onClick = { onNavigate(item.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF4CAF50),
                    unselectedIconColor = Color.Gray,
                    indicatorColor = Color.White
                )
            )
        }
    }
}

@Composable
private fun currentRoute(navController: NavHostController): String? {
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
        route = AppRoutes.MEMBER_WORKOUT
    ),
    BottomNavItem(
        label = "Events",
        icon = Icons.Default.Person,
        route = AppRoutes.MEMBER_EVENT
    ),
    BottomNavItem(
        label = "Profile",
        icon = Icons.Default.Person,
        route = AppRoutes.MEMBER_PROFILE
    )
)

