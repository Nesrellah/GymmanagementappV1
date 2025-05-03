package com.example.gymmanagement.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymmanagement.data.model.Workout
import com.example.gymmanagement.data.repository.WorkoutRepository
import com.example.gymmanagement.data.repository.UserRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MemberWorkoutViewModel(
    private val repository: WorkoutRepository,
    private val userRepository: UserRepository,
    private val currentUserEmail: String
) : ViewModel() {

    private val _traineeId = MutableStateFlow<Int?>(null)
    val traineeId: StateFlow<Int?> = _traineeId.asStateFlow()

    private val _workouts = MutableStateFlow<List<Workout>>(emptyList())
    val workouts: StateFlow<List<Workout>> = _workouts.asStateFlow()

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress.asStateFlow()

    init {
        // Load traineeId
        viewModelScope.launch {
            try {
                val userProfile = userRepository.getUserProfileByEmail(currentUserEmail)
                _traineeId.value = userProfile?.id
                Log.d("MemberWorkoutViewModel", "Loaded traineeId: ${_traineeId.value}")
            } catch (e: Exception) {
                Log.e("MemberWorkoutViewModel", "Error loading traineeId", e)
            }
        }

        // Reactively load workouts when traineeId is available
        viewModelScope.launch {
            traineeId.filterNotNull().collect { id ->
                repository.getWorkoutsByTraineeId(id)
                    .onEach { workouts ->
                        Log.d("MemberWorkoutViewModel", "Received workouts: $workouts")
                        _workouts.value = workouts
                        updateProgress(workouts)
                    }
                    .catch { e ->
                        Log.e("MemberWorkoutViewModel", "Error fetching workouts", e)
                    }
                    .collect()
            }
        }
    }

    fun toggleWorkoutCompletion(workout: Workout) {
        viewModelScope.launch {
            try {
                val updatedWorkout = workout.copy(isCompleted = !workout.isCompleted)
                repository.updateWorkout(updatedWorkout)
                Log.d("MemberWorkoutViewModel", "Workout completion toggled: ${updatedWorkout.isCompleted}")
            } catch (e: Exception) {
                Log.e("MemberWorkoutViewModel", "Error toggling workout completion", e)
            }
        }
    }

    private fun updateProgress(workouts: List<Workout>) {
        if (workouts.isEmpty()) {
            _progress.value = 0f
            return
        }
        val completedCount = workouts.count { it.isCompleted }
        _progress.value = completedCount.toFloat() / workouts.size
        Log.d("MemberWorkoutViewModel", "Progress updated: ${_progress.value}")
    }
} 