package com.example.gymmanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymmanagement.ui.navigation.AppNavigation
import com.example.gymmanagement.ui.theme.GymManagementTheme
import com.example.gymmanagement.viewmodel.AdminWorkoutViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GymManagementTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val workoutViewModel: AdminWorkoutViewModel = viewModel()
                    AppNavigation(workoutViewModel = workoutViewModel)
                }
            }
        }
    }
}

