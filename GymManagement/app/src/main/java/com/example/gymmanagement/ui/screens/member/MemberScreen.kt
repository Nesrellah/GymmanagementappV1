package com.example.gymmanagement.ui.screens.member

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gymmanagement.navigation.AppRoutes
import com.example.gymmanagement.ui.screens.member.workout.MemberWorkoutScreen
import com.example.gymmanagement.data.dao.MemberWorkoutDao
import com.example.gymmanagement.data.repository.MemberWorkoutRepositoryImpl
import com.example.gymmanagement.viewmodel.MemberWorkoutViewModel
import kotlinx.coroutines.flow.flowOf
import com.example.gymmanagement.data.model.MemberWorkout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberScreen() {
    val navController = rememberNavController()
    val currentRoute = currentRoute(navController)

    // Initialize DAO (in a real app, this would be from Room database)
    val memberWorkoutDao = remember {
        object : MemberWorkoutDao {
            override fun getWorkoutsForTrainee(traineeId: String) = flowOf(MemberWorkoutViewModel.previewWorkouts)
            override fun getAllWorkouts() = flowOf(MemberWorkoutViewModel.previewWorkouts)
            override suspend fun insertWorkout(workout: MemberWorkout) {}
            override suspend fun updateWorkout(workout: MemberWorkout) {}
            override suspend fun deleteWorkout(workout: MemberWorkout) {}
            override suspend fun updateWorkoutCompletion(workoutId: Int, isCompleted: Boolean) {}
            override fun getCompletedWorkoutsCount(traineeId: String) = flowOf(1)
            override fun getTotalWorkoutsCount(traineeId: String) = flowOf(3)
        }
    }

    // Initialize repository with DAO
    val repository = remember { MemberWorkoutRepositoryImpl(memberWorkoutDao) }

    // Initialize ViewModel with repository
    val viewModel = remember { MemberWorkoutViewModel(repository) }

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
            MemberNavigation(
                navController = navController,
                viewModel = viewModel
            )
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
fun MemberNavigation(
    navController: NavHostController,
    viewModel: MemberWorkoutViewModel
) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.MEMBER_WORKOUT
    ) {
        composable(AppRoutes.MEMBER_WORKOUT) {
            MemberWorkoutScreen(viewModel = viewModel)
        }
        composable(AppRoutes.MEMBER_EVENT) {
            // TODO: Add MemberEventScreen
        }
        composable(AppRoutes.MEMBER_PROFILE) {
            // TODO: Add MemberProfileScreen
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