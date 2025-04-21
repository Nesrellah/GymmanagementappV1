package com.example.gymmanagementapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val email: String,
    val passwordHash: String,
    val age: Int,
    val height: Float, // in cm
    val weight: Float, // in kg
    val isAdmin: Boolean = false,
    val joinDate: Long = System.currentTimeMillis()
)