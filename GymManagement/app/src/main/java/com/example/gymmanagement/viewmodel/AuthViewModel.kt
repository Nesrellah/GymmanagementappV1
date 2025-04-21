package com.example.gymmanagement.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymmanagement.data.db.AppDatabase
import com.example.gymmanagement.data.model.UserEntity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = AppDatabase.getDatabase(application).userDao()

    fun registerUser(
        name: String, email: String, password: String,
        age: Int, height: Float, weight: Float,
        role: String = "member", // Default to member
        onComplete: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            val existingUser = userDao.getUserByEmail(email)
            if (existingUser != null) {
                onComplete(false, "User already exists")
                return@launch
            }

            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            val newUser = UserEntity(
                name = name,
                email = email,
                password = password,
                age = age,
                height = height,
                weight = weight,
                role = role,
                joinDate = date
            )

            userDao.insertUser(newUser)
            onComplete(true, "Registered successfully")
        }
    }

    fun login(email: String, password: String, onResult: (UserEntity?) -> Unit) {
        viewModelScope.launch {
            val user = userDao.login(email, password)
            onResult(user)
        }
    }
}
