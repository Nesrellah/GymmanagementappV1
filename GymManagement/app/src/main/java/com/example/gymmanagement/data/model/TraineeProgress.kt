package com.example.gymmanagement.data.model

data class TraineeProgress(
    val id: Int = 0,
    val name: String,
    val email: String,
    val completedWorkouts: Int = 0,
    val totalWorkouts: Int = 0
) {
    val progressPercentage: Int
        get() = if (totalWorkouts > 0) (completedWorkouts * 100) / totalWorkouts else 0
} 