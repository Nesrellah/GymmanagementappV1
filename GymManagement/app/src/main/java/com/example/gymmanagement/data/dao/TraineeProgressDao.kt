package com.example.gymmanagement.data.dao

import androidx.room.*
import com.example.gymmanagement.data.model.TraineeProgress
import kotlinx.coroutines.flow.Flow

@Dao
interface TraineeProgressDao {
    @Query("""
        SELECT * FROM trainee_progress tp1
        WHERE lastUpdated = (
            SELECT MAX(lastUpdated)
            FROM trainee_progress tp2
            WHERE tp2.traineeId = tp1.traineeId
        )
        ORDER BY lastUpdated DESC
    """)
    fun getAllProgress(): Flow<List<TraineeProgress>>

    @Query("SELECT * FROM trainee_progress WHERE id = :id")
    suspend fun getProgressById(id: Int): TraineeProgress?

    @Query("""
        SELECT * FROM trainee_progress
        WHERE traineeId = :traineeId
        ORDER BY lastUpdated DESC
        LIMIT 1
    """)
    fun getProgressByTraineeId(traineeId: String): Flow<List<TraineeProgress>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: TraineeProgress)

    @Update
    suspend fun updateProgress(progress: TraineeProgress)

    @Delete
    suspend fun deleteProgress(progress: TraineeProgress)
} 