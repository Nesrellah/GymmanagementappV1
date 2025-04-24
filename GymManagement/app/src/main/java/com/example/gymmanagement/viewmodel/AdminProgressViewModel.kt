package com.example.gymmanagement.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymmanagement.data.model.TraineeProgress
import com.example.gymmanagement.data.repository.TraineeProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AdminProgressViewModel(
    private val repository: TraineeProgressRepository
) : ViewModel() {
    val allProgress: Flow<List<TraineeProgress>> = repository.getAllProgress()

    fun updateProgress(progress: TraineeProgress) {
        viewModelScope.launch {
            repository.updateProgress(progress)
        }
    }

    fun insertProgress(progress: TraineeProgress) {
        viewModelScope.launch {
            repository.insertProgress(progress)
        }
    }

    fun deleteProgress(progress: TraineeProgress) {
        viewModelScope.launch {
            repository.deleteProgress(progress)
        }
    }
} 