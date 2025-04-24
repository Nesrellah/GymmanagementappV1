package com.example.gymmanagement.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "member_workouts")
data class MemberWorkout(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val sets: Int,
    val repsOrSecs: Int,
    val restTime: Int,
    val imageUri: String? = null,
    val isCompleted: Boolean = false,
    val traineeId: String // To associate workouts with specific members
) 