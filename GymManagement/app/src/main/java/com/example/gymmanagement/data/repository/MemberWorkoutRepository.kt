package com.example.gymmanagement.data.repository

import com.example.gymmanagement.data.dao.MemberWorkoutDao
import com.example.gymmanagement.data.model.MemberWorkout
import kotlinx.coroutines.flow.Flow

interface MemberWorkoutRepository {
    fun getWorkoutsForTrainee(traineeId: String): Flow<List<MemberWorkout>>
    fun getAllWorkouts(): Flow<List<MemberWorkout>>
    suspend fun insertWorkout(workout: MemberWorkout)
    suspend fun updateWorkout(workout: MemberWorkout)
    suspend fun deleteWorkout(workout: MemberWorkout)
    suspend fun updateWorkoutCompletion(workoutId: Int, isCompleted: Boolean)
    fun getCompletedWorkoutsCount(traineeId: String): Flow<Int>
    fun getTotalWorkoutsCount(traineeId: String): Flow<Int>
}

class MemberWorkoutRepositoryImpl(
    private val memberWorkoutDao: MemberWorkoutDao
) : MemberWorkoutRepository {
    override fun getWorkoutsForTrainee(traineeId: String): Flow<List<MemberWorkout>> =
        memberWorkoutDao.getWorkoutsForTrainee(traineeId)

    override fun getAllWorkouts(): Flow<List<MemberWorkout>> =
        memberWorkoutDao.getAllWorkouts()

    override suspend fun insertWorkout(workout: MemberWorkout) =
        memberWorkoutDao.insertWorkout(workout)

    override suspend fun updateWorkout(workout: MemberWorkout) =
        memberWorkoutDao.updateWorkout(workout)

    override suspend fun deleteWorkout(workout: MemberWorkout) =
        memberWorkoutDao.deleteWorkout(workout)

    override suspend fun updateWorkoutCompletion(workoutId: Int, isCompleted: Boolean) =
        memberWorkoutDao.updateWorkoutCompletion(workoutId, isCompleted)

    override fun getCompletedWorkoutsCount(traineeId: String): Flow<Int> =
        memberWorkoutDao.getCompletedWorkoutsCount(traineeId)

    override fun getTotalWorkoutsCount(traineeId: String): Flow<Int> =
        memberWorkoutDao.getTotalWorkoutsCount(traineeId)
} 