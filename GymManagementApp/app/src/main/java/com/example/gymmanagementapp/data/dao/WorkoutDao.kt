package com.example.gymmanagementapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.gymmanagementapp.data.entities.Workout

@Dao
interface WorkoutDao {
    @Insert
    suspend fun insertWorkout(workout: Workout)

    @Query("SELECT * FROM workouts WHERE traineeId = :userId")
    suspend fun getWorkoutsForUser(userId: String): List<Workout>

    @Delete
    suspend fun deleteWorkout(workout: Workout)
}