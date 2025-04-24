package com.example.gymmanagement.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.gymmanagement.data.database.AppDatabase
import com.example.gymmanagement.data.model.EventEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*

class MemberEventViewModel(application: Application) : AndroidViewModel(application) {
    private val eventDao = AppDatabase.getDatabase(application).eventDao()
    
    // Get all events for now, but in a real app you might want to filter by date
    val upcomingEvents: Flow<List<EventEntity>> = eventDao.getAllEvents()
    
    // You can add methods here to filter events by date if needed
    fun isEventUpcoming(eventDate: String): Boolean {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val eventCalendar = Calendar.getInstance().apply {
            time = dateFormat.parse(eventDate) ?: return false
        }
        val today = Calendar.getInstance()
        return !eventCalendar.before(today)
    }
} 