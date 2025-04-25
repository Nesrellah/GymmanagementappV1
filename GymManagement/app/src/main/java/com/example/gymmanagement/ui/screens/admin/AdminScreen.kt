package com.example.gymmanagement.ui.screens.admin

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

@Composable
fun AdminScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {
    var selectedTab by remember { mutableStateOf(0) }
    
    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            selectedTab = when (destination.route) {
                AppRoutes.ADMIN_WORKOUT -> 0
                AppRoutes.ADMIN_EVENT -> 1
                AppRoutes.ADMIN_PROGRESS -> 3
                else -> selectedTab
            }
        }
    }
    
    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { 
                    selectedTab = 0
                    navController.navigate(AppRoutes.ADMIN_WORKOUT)
                },
                text = { Text("Workouts") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { 
                    selectedTab = 1
                    navController.navigate(AppRoutes.ADMIN_EVENT)
                },
                text = { Text("Events") }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("Members") }
            )
            Tab(
                selected = selectedTab == 3,
                onClick = { 
                    selectedTab = 3
                    navController.navigate(AppRoutes.ADMIN_PROGRESS)
                },
                text = { Text("Progress") }
            )
        }
        
        when (selectedTab) {
            0 -> AdminWorkoutScreen(
                onNavigateToEvents = { 
                    selectedTab = 1
                    navController.navigate(AppRoutes.ADMIN_EVENT)
                },
                onNavigateToProgress = { 
                    selectedTab = 3
                    navController.navigate(AppRoutes.ADMIN_PROGRESS)
                }
            )
            1 -> AdminEventScreen(
                onNavigateToWorkouts = { 
                    selectedTab = 0
                    navController.navigate(AppRoutes.ADMIN_WORKOUT)
                },
                onNavigateToProgress = { 
                    selectedTab = 3
                    navController.navigate(AppRoutes.ADMIN_PROGRESS)
                }
            )
            2 -> AdminMemberScreen()
            3 -> AdminProgressScreen()
        }
    }
} 