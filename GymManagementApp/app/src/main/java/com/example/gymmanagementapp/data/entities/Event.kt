package com.example.gymmanagementapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "events")
data class Event(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val date: String, // e.g., "2025-04-15"
    val time: String // e.g., "18:00"
)
