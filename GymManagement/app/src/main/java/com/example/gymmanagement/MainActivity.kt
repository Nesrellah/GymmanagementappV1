package com.example.gymmanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.gymmanagement.data.dao.MemberWorkoutDao
import com.example.gymmanagement.data.repository.MemberWorkoutRepositoryImpl
import com.example.gymmanagement.data.model.MemberWorkout
import com.example.gymmanagement.ui.screens.member.workout.MemberWorkoutScreen
import com.example.gymmanagement.ui.theme.GymManagementAppTheme
import com.example.gymmanagement.viewmodel.MemberWorkoutViewModel
import kotlinx.coroutines.flow.flowOf

// MainActivity.kt
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize DAO (in a real app, this would be from Room database)
        val memberWorkoutDao = object : MemberWorkoutDao {
            override fun getWorkoutsForTrainee(traineeId: String) = flowOf(MemberWorkoutViewModel.previewWorkouts)
            override fun getAllWorkouts() = flowOf(MemberWorkoutViewModel.previewWorkouts)
            override suspend fun insertWorkout(workout: MemberWorkout) {}
            override suspend fun updateWorkout(workout: MemberWorkout) {}
            override suspend fun deleteWorkout(workout: MemberWorkout) {}
            override suspend fun updateWorkoutCompletion(workoutId: Int, isCompleted: Boolean) {}
            override fun getCompletedWorkoutsCount(traineeId: String) = flowOf(1)
            override fun getTotalWorkoutsCount(traineeId: String) = flowOf(3)
        }

        // Initialize repository with DAO
        val repository = MemberWorkoutRepositoryImpl(memberWorkoutDao)

        // Initialize ViewModel with repository
        val viewModel = MemberWorkoutViewModel(repository)

        setContent {
            GymManagementAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MemberWorkoutScreen(viewModel = viewModel)
                }
            }
        }
    }
}


