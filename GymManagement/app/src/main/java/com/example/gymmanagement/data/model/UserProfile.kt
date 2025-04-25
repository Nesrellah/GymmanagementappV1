package com.example.gymmanagement.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val email: String,
    val name: String,
    val phone: String? = null,
    val address: String? = null,
    val age: Int? = null,
    val height: Float? = null,
    val weight: Float? = null,
    val bmi: Float? = null,
    val role: String,
    val joinDate: String? = null,
    val membershipStatus: String = "active"
) 