package com.example.gymmanagement.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "workouts",
    foreignKeys = [
        ForeignKey(
            entity = UserProfile::class,
            parentColumns = ["id"],
            childColumns = ["traineeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("traineeId")]
)
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val eventTitle: String,
    val traineeId: Int,
    val sets: Int,
    val repsOrSecs: Int,
    val restTime: Int,
    val imageUri: String? = null,
    val isCompleted: Boolean = false
) 