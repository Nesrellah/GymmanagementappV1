package com.example.gymmanagement.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymmanagement.data.db.AppDatabase
import com.example.gymmanagement.data.model.Workout
import com.example.gymmanagement.data.repository.WorkoutRepository
import com.example.gymmanagement.utils.ImagePicker
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AdminWorkoutViewModel(application:    Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val workoutDao = database.workoutDao()
    private val repository = WorkoutRepository(workoutDao)
    private val imagePicker = ImagePicker(application)

    private val _workouts = workoutDao.getAllWorkouts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val workouts: StateFlow<List<Workout>> = _workouts

    fun addWorkout(workout: Workout) {
        viewModelScope.launch {
            repository.insertWorkout(workout)
        }
    }

    fun updateWorkout(workout: Workout) {
        viewModelScope.launch {
            repository.updateWorkout(workout)
        }
    }

    fun deleteWorkout(workout: Workout) {
        viewModelScope.launch {
            repository.deleteWorkout(workout)
        }
    }

    fun handleImageSelection(uri: Uri): String? {
        return imagePicker.saveImageToInternalStorage(uri)
    }
} 