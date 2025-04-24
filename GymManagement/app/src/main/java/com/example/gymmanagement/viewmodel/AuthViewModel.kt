package com.example.gymmanagement.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gymmanagement.data.database.AppDatabase
import com.example.gymmanagement.data.model.UserEntity
import com.example.gymmanagement.data.model.UserProfile
import com.example.gymmanagement.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AuthViewModel(
    private val repository: UserRepository
) : ViewModel() {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _currentUser = MutableStateFlow<UserProfile?>(null)
    val currentUser: StateFlow<UserProfile?> = _currentUser

    fun registerUser(
        name: String, email: String, password: String,
        age: Int, height: Float, weight: Float,
        role: String = "member", // Default to member
        onComplete: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            val existingUser = repository.getUserByEmail(email)
            if (existingUser != null) {
                onComplete(false, "User already exists")
                return@launch
            }

            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            val newUser = UserEntity(
                id = 0,
                name = name,
                email = email,
                password = password,
                age = age,
                height = height,
                weight = weight,
                role = role,
                joinDate = date
            )

            repository.insertUser(newUser)

            // Create corresponding UserProfile
            val userProfile = UserProfile(
                id = 0,
                email = email,
                name = name,
                age = age,
                height = height.toInt(),
                weight = weight.toInt(),
                bmi = calculateBMI(height, weight),
                joinDate = date,
                role = role
            )

            repository.insertUserProfile(userProfile)
            onComplete(true, "Registered successfully")
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            repository.login(email, password)?.let { user ->
                _isLoggedIn.value = true
                repository.getUserProfileByEmail(email)?.let { profile ->
                    _currentUser.value = profile
                }
            }
        }
    }

    fun register(
        email: String,
        password: String,
        name: String,
        phone: String? = null,
        address: String? = null,
        role: String
    ) {
        viewModelScope.launch {
            val user = repository.getUserByEmail(email)
            if (user == null) {
                val profile = UserProfile(
                    id = 0,
                    email = email,
                    name = name,
                    phone = phone,
                    address = address,
                    role = role
                )
                repository.insertUserProfile(profile)
                _currentUser.value = profile
                _isLoggedIn.value = true
            }
        }
    }

    fun logout() {
        _isLoggedIn.value = false
        _currentUser.value = null
    }

    private fun calculateBMI(height: Float, weight: Float): Double {
        val heightM = height / 100
        return (weight / (heightM * heightM)).toDouble()
    }

    class Factory(
        private val repository: UserRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                return AuthViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
