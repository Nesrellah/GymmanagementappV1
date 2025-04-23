package com.example.gymmanagement.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val eventTitle: String,
    val traineeId: String,
    val sets: Int,
    val repsOrSecs: Int,
    val restTime: Int,
    val imageUri: String? = null,
    val isCompleted: Boolean = false
) 