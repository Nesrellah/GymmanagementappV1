package com.example.gymmanagement.data.repository

import android.util.Log
import com.example.gymmanagement.data.dao.WorkoutDao
import com.example.gymmanagement.data.model.Workout
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    fun getAllWorkouts(): Flow<List<Workout>>
    suspend fun getWorkoutById(id: Int): Workout?
    fun getWorkoutsByTraineeId(traineeId: String): Flow<List<Workout>>
    suspend fun insertWorkout(workout: Workout)
    suspend fun updateWorkout(workout: Workout)
    suspend fun deleteWorkout(workout: Workout)
}

class WorkoutRepositoryImpl(
    private val workoutDao: WorkoutDao
) : WorkoutRepository {
    override fun getAllWorkouts(): Flow<List<Workout>> {
        Log.d("WorkoutRepository", "Getting all workouts")
        return workoutDao.getAllWorkouts()
    }
    
    override suspend fun getWorkoutById(id: Int): Workout? {
        Log.d("WorkoutRepository", "Getting workout by id: $id")
        return workoutDao.getWorkoutById(id)
    }
    
    override fun getWorkoutsByTraineeId(traineeId: String): Flow<List<Workout>> {
        Log.d("WorkoutRepository", "Getting workouts for traineeId: $traineeId")
        return workoutDao.getWorkoutsByTraineeId(traineeId)
    }
    
    override suspend fun insertWorkout(workout: Workout) {
        Log.d("WorkoutRepository", "Inserting workout: $workout")
        workoutDao.insertWorkout(workout)
    }
    
    override suspend fun updateWorkout(workout: Workout) {
        Log.d("WorkoutRepository", "Updating workout: $workout")
        workoutDao.updateWorkout(workout)
    }
    
    override suspend fun deleteWorkout(workout: Workout) {
        Log.d("WorkoutRepository", "Deleting workout: $workout")
        workoutDao.deleteWorkout(workout)
    }
} 