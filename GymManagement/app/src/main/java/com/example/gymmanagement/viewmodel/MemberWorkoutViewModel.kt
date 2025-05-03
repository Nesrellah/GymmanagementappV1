package com.example.gymmanagement.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymmanagement.data.model.Workout
import com.example.gymmanagement.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MemberWorkoutViewModel(
    private val repository: WorkoutRepository,
    private val currentUserEmail: String
) : ViewModel() {

    init {
        Log.d("MemberWorkoutViewModel", "Initializing with currentUserEmail: $currentUserEmail")
    }

    private val _workouts = repository.getWorkoutsByTraineeId(currentUserEmail)
        .onEach { workouts ->
            Log.d("MemberWorkoutViewModel", "Received workouts: $workouts")
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    val workouts: StateFlow<List<Workout>> = _workouts

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress.asStateFlow()

    init {
        viewModelScope.launch {
            _workouts.collect { workouts ->
                Log.d("MemberWorkoutViewModel", "Updating progress for workouts: $workouts")
                updateProgress(workouts)
            }
        }
    }

    fun toggleWorkoutCompletion(workout: Workout) {
        viewModelScope.launch {
            try {
                val updatedWorkout = workout.copy(isCompleted = !workout.isCompleted)
                repository.updateWorkout(updatedWorkout)
                // No need to reload workouts as Flow will update automatically
            } catch (e: Exception) {
                // Handle error
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
    }
} 