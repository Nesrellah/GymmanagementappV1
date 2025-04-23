package com.example.gymmanagement.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymmanagement.data.dao.EventDao
import com.example.gymmanagement.data.model.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminEventViewModel(
    private val eventDao: EventDao
) : ViewModel() {
    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events.asStateFlow()

    var selectedImageUri by mutableStateOf<Uri?>(null)
        private set

    init {
        loadEvents()
    }

    private fun loadEvents() {
        viewModelScope.launch {
            eventDao.getAllEvents().collect {
                _events.value = it
            }
        }
    }

    fun setSelectedImage(uri: Uri?) {
        selectedImageUri = uri
    }

    fun addEvent(event: Event) {
        viewModelScope.launch {
            eventDao.insertEvent(event)
        }
    }

    fun updateEvent(event: Event) {
        viewModelScope.launch {
            eventDao.updateEvent(event)
        }
    }

    fun deleteEvent(event: Event) {
        viewModelScope.launch {
            eventDao.deleteEvent(event)
        }
    }
} 