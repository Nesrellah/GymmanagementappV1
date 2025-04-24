package com.example.gymmanagement.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gymmanagement.data.model.MemberWorkout
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberWorkoutDao {
    @Query("SELECT * FROM member_workouts WHERE traineeId = :traineeId")
    fun getWorkoutsForTrainee(traineeId: String): Flow<List<MemberWorkout>>

    @Query("SELECT * FROM member_workouts")
    fun getAllWorkouts(): Flow<List<MemberWorkout>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: MemberWorkout)

    @Update
    suspend fun updateWorkout(workout: MemberWorkout)

    @Delete
    suspend fun deleteWorkout(workout: MemberWorkout)

    @Query("UPDATE member_workouts SET isCompleted = :isCompleted WHERE id = :workoutId")
    suspend fun updateWorkoutCompletion(workoutId: Int, isCompleted: Boolean)

    @Query("SELECT COUNT(*) FROM member_workouts WHERE traineeId = :traineeId AND isCompleted = 1")
    fun getCompletedWorkoutsCount(traineeId: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM member_workouts WHERE traineeId = :traineeId")
    fun getTotalWorkoutsCount(traineeId: String): Flow<Int>
} 