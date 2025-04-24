package com.example.gymmanagement.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymmanagement.data.database.AppDatabase
import com.example.gymmanagement.data.model.EventEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AdminEventViewModel(application: Application) : AndroidViewModel(application) {
    private val eventDao = AppDatabase.getDatabase(application).eventDao()
    val events: Flow<List<EventEntity>> = eventDao.getAllEvents()

    fun addEvent(event: EventEntity) {
        viewModelScope.launch {
            eventDao.insertEvent(event)
        }
    }

    fun updateEvent(event: EventEntity) {
        viewModelScope.launch {
            eventDao.updateEvent(event)
        }
    }

    fun deleteEvent(event: EventEntity) {
        viewModelScope.launch {
            eventDao.deleteEvent(event)
        }
    }
} 