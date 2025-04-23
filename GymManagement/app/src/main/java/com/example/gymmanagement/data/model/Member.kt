package com.example.gymmanagement.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "member")
data class Member(
    @PrimaryKey
    val id: Int,
    val name: String,
    val email: String,
    val age: Int,
    val height: Float,  // in cm
    val weight: Float,  // in kg
    val bmi: Float = weight / ((height/100) * (height/100))  // Auto-calculated BMI
) 