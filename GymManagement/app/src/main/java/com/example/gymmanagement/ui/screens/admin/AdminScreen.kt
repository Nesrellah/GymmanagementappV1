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

@Composable
fun AdminScreen(
    viewModel: AdminViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf(0) }
    
    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Workouts") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Events") }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("Members") }
            )
            Tab(
                selected = selectedTab == 3,
                onClick = { selectedTab = 3 },
                text = { Text("Progress") }
            )
        }
        
        when (selectedTab) {
            0 -> AdminWorkoutScreen(
                onNavigateToEvents = {},
                onNavigateToProgress = {}
            )
            1 -> AdminEventScreen(
                onNavigateToWorkouts = {},
                onNavigateToProgress = {}
            )
            2 -> AdminMemberScreen()
            3 -> AdminProgressScreen(viewModel = viewModel())
        }
    }
} 