package com.example.gymmanagement.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymmanagement.data.model.MemberWorkout
import com.example.gymmanagement.data.repository.MemberWorkoutRepositoryImpl
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MemberWorkoutViewModel(
    private val repository: MemberWorkoutRepositoryImpl
) : ViewModel() {

    private val _workouts = MutableStateFlow<List<MemberWorkout>>(emptyList())
    val workouts: StateFlow<List<MemberWorkout>> = _workouts.asStateFlow()

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress.asStateFlow()

    // This would come from user session in a real app
    private val currentTraineeId = MutableStateFlow("TR001")

    init {
        loadWorkouts()
    }

    private fun loadWorkouts() {
        viewModelScope.launch {
            try {
                repository.getWorkoutsForTrainee(currentTraineeId.value)
                    .collect { workoutList ->
                        _workouts.value = workoutList
                        updateProgress(workoutList)
                    }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun toggleWorkoutCompletion(workout: MemberWorkout) {
        viewModelScope.launch {
            try {
                val updatedWorkout = workout.copy(isCompleted = !workout.isCompleted)
                repository.updateWorkout(updatedWorkout)
                // No need to reload as Flow will emit new value automatically
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private fun updateProgress(workouts: List<MemberWorkout>) {
        if (workouts.isEmpty()) {
            _progress.value = 0f
            return
        }
        val completedCount = workouts.count { it.isCompleted }
        _progress.value = completedCount.toFloat() / workouts.size
    }

    companion object {
        // Preview data
        val previewWorkouts = listOf(
            MemberWorkout(
                id = 1,
                title = "Dead Lift",
                sets = 4,
                repsOrSecs = 10,
                restTime = 60,
                imageUri = null,
                traineeId = "TR001",
                isCompleted = false
            ),
            MemberWorkout(
                id = 2,
                title = "Bench Press",
                sets = 3,
                repsOrSecs = 12,
                restTime = 90,
                imageUri = null,
                traineeId = "TR001",
                isCompleted = true
            ),
            MemberWorkout(
                id = 3,
                title = "Squats",
                sets = 4,
                repsOrSecs = 15,
                restTime = 120,
                imageUri = null,
                traineeId = "TR001",
                isCompleted = false
            )
        )
    }
} 