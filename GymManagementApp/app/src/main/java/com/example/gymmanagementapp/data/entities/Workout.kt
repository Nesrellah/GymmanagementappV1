package com.example.gymmanagementapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val traineeId: String, // Links to User.id
    val sets: Int,
    val reps: Int,
    val restTime: Int // seconds
)
