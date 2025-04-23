package com.example.gymmanagement.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymmanagement.data.model.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminEventViewModel : ViewModel() {
    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    fun addEvent(event: Event) {
        viewModelScope.launch {
            _events.value = _events.value + event
        }
    }

    fun updateEvent(event: Event) {
        viewModelScope.launch {
            _events.value = _events.value.map { 
                if (it.id == event.id) event else it 
            }
        }
    }

    fun deleteEvent(event: Event) {
        viewModelScope.launch {
            _events.value = _events.value.filter { it.id != event.id }
        }
    }
} 