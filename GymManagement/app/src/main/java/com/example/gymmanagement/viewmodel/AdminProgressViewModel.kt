package com.example.gymmanagement.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gymmanagement.data.model.TraineeProgress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AdminProgressViewModel : ViewModel() {
    private val _trainees = MutableStateFlow<List<TraineeProgress>>(emptyList())
    val trainees: StateFlow<List<TraineeProgress>> = _trainees

    init {
        // For demonstration, populate with sample data
        // In a real app, this would come from your database
        _trainees.value = listOf(
            TraineeProgress(
                id = 1,
                name = "Abel Melaku",
                email = "abel@example.com",
                completedWorkouts = 3,
                totalWorkouts = 5
            ),
            TraineeProgress(
                id = 2,
                name = "Meron Nebyu",
                email = "meron@example.com",
                completedWorkouts = 4,
                totalWorkouts = 5
            ),
            TraineeProgress(
                id = 3,
                name = "Hana Hagos",
                email = "hana@example.com",
                completedWorkouts = 9,
                totalWorkouts = 10
            )
        )
    }

    fun updateTraineeProgress(traineeId: Int, completedWorkouts: Int) {
        val updatedTrainees = _trainees.value.map { trainee ->
            if (trainee.id == traineeId) {
                trainee.copy(completedWorkouts = completedWorkouts)
            } else {
                trainee
            }
        }
        _trainees.value = updatedTrainees
    }
} 