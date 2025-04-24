package com.example.gymmanagement.data.repository

import com.example.gymmanagement.data.dao.TraineeProgressDao
import com.example.gymmanagement.data.model.TraineeProgress
import kotlinx.coroutines.flow.Flow

interface TraineeProgressRepository {
    fun getAllProgress(): Flow<List<TraineeProgress>>
    suspend fun getProgressById(id: Int): TraineeProgress?
    fun getProgressByTraineeId(traineeId: String): Flow<List<TraineeProgress>>
    suspend fun insertProgress(progress: TraineeProgress)
    suspend fun updateProgress(progress: TraineeProgress)
    suspend fun deleteProgress(progress: TraineeProgress)
}

class TraineeProgressRepositoryImpl(
    private val traineeProgressDao: TraineeProgressDao
) : TraineeProgressRepository {
    override fun getAllProgress(): Flow<List<TraineeProgress>> = traineeProgressDao.getAllProgress()
    
    override suspend fun getProgressById(id: Int): TraineeProgress? = traineeProgressDao.getProgressById(id)
    
    override fun getProgressByTraineeId(traineeId: String): Flow<List<TraineeProgress>> = 
        traineeProgressDao.getProgressByTraineeId(traineeId)
    
    override suspend fun insertProgress(progress: TraineeProgress) = traineeProgressDao.insertProgress(progress)
    
    override suspend fun updateProgress(progress: TraineeProgress) = traineeProgressDao.updateProgress(progress)
    
    override suspend fun deleteProgress(progress: TraineeProgress) = traineeProgressDao.deleteProgress(progress)
} 