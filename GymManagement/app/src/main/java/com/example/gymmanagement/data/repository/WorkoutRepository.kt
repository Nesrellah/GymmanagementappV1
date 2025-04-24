package com.example.gymmanagement.data.repository

import com.example.gymmanagement.data.dao.WorkoutDao
import com.example.gymmanagement.data.model.Workout
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    fun getAllWorkouts(): Flow<List<Workout>>
    suspend fun getWorkoutById(id: Int): Workout?
    suspend fun getWorkoutsByTraineeId(traineeId: String): List<Workout>
    suspend fun insertWorkout(workout: Workout)
    suspend fun updateWorkout(workout: Workout)
    suspend fun deleteWorkout(workout: Workout)
}

class WorkoutRepositoryImpl(
    private val workoutDao: WorkoutDao
) : WorkoutRepository {
    override fun getAllWorkouts(): Flow<List<Workout>> = workoutDao.getAllWorkouts()
    
    override suspend fun getWorkoutById(id: Int): Workout? = workoutDao.getWorkoutById(id)
    
    override suspend fun getWorkoutsByTraineeId(traineeId: String): List<Workout> = 
        workoutDao.getWorkoutsByTraineeId(traineeId)
    
    override suspend fun insertWorkout(workout: Workout) = workoutDao.insertWorkout(workout)
    
    override suspend fun updateWorkout(workout: Workout) = workoutDao.updateWorkout(workout)
    
    override suspend fun deleteWorkout(workout: Workout) = workoutDao.deleteWorkout(workout)
} 