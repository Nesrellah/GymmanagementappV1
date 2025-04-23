package com.example.gymmanagement.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val password: String,
    val age: Int,
    val height: Float,
    val weight: Float,
    val role: String, // "admin" or "member"
    val joinDate: String
)

