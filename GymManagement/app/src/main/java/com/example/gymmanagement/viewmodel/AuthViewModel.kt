package com.example.gymmanagement.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gymmanagement.GymManagementApp
import com.example.gymmanagement.data.model.UserEntity
import com.example.gymmanagement.data.model.UserProfile
import com.example.gymmanagement.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class AuthViewModel(
    private val app: GymManagementApp,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError

    private val _registerError = MutableStateFlow<String?>(null)
    val registerError: StateFlow<String?> = _registerError

    private val sharedPreferences = app.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    init {
        loadSession()
    }

    private fun loadSession() {
        val email = sharedPreferences.getString("user_email", null)
        val role = sharedPreferences.getString("user_role", null)
        
        if (email != null && role != null) {
            viewModelScope.launch {
                try {
                    val user = userRepository.getUserByEmail(email)
                    if (user != null && user.role.lowercase() != "admin" && user.role.lowercase() == role.lowercase()) {
                        _currentUser.value = user
                        _isLoggedIn.value = true
                        Log.d("AuthViewModel", "Session restored for user: $email with role: $role")
                    } else {
                        clearSession()
                        Log.d("AuthViewModel", "Invalid session data found or admin user, clearing session")
                    }
                } catch (e: Exception) {
                    Log.e("AuthViewModel", "Error loading session", e)
                    clearSession()
                }
            }
        }
    }

    fun checkLoginState() {
        viewModelScope.launch {
            try {
                val email = sharedPreferences.getString("user_email", null)
                if (email != null) {
                    val user = userRepository.getUserByEmail(email)
                    if (user != null) {
                        _currentUser.value = user
                        _isLoggedIn.value = true
                        Log.d("AuthViewModel", "Login state checked: User is logged in")
                    } else {
                        clearSession()
                        Log.d("AuthViewModel", "Login state checked: User not found")
                    }
                } else {
                    clearSession()
                    Log.d("AuthViewModel", "Login state checked: No session found")
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error checking login state", e)
                clearSession()
            }
        }
    }

    fun validateEmail(email: String): String? {
        if (email.isEmpty()) return "Email address is required"
        val emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        if (!emailPattern.matcher(email).matches()) {
            return "Please enter a valid email address (e.g., user@example.com)"
        }
        return null
    }

    fun validatePassword(password: String): String? {
        if (password.isEmpty()) return "Password is required"
        if (password.length < 6) return "Password must be at least 6 characters long"
        return null
    }

    fun validateName(name: String): String? {
        if (name.isEmpty()) return "Full name is required"
        if (name.length < 2) return "Name must be at least 2 characters long"
        if (!name.matches(Regex("^[a-zA-Z\\s]*$"))) return "Name can only contain letters and spaces"
        return null
    }

    fun validateAge(age: String): String? {
        if (age.isEmpty()) return "Age is required"
        val ageInt = age.toIntOrNull()
        if (ageInt == null) return "Please enter a valid number for age"
        if (ageInt < 10) return "You must be at least 10 years old to register"
        if (ageInt > 100) return "Please enter a valid age (maximum 100 years)"
        return null
    }

    fun validateHeight(height: String): String? {
        if (height.isEmpty()) return "Height is required"
        val heightFloat = height.toFloatOrNull()
        if (heightFloat == null) return "Please enter a valid number for height"
        if (heightFloat < 80) return "Height must be at least 80 cm"
        if (heightFloat > 250) return "Height cannot exceed 250 cm"
        return null
    }

    fun validateWeight(weight: String): String? {
        if (weight.isEmpty()) return "Weight is required"
        val weightFloat = weight.toFloatOrNull()
        if (weightFloat == null) return "Please enter a valid number for weight"
        if (weightFloat < 35) return "Weight must be at least 35 kg"
        if (weightFloat > 200) return "Weight cannot exceed 200 kg"
        return null
    }

    fun register(
        email: String,
        password: String,
        name: String,
        role: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val existingUser = userRepository.getUserByEmail(email)
                if (existingUser != null) {
                    onError("Email already registered")
                    return@launch
                }

                val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val newUser = UserEntity(
                    id = 0,
                    name = name,
                    email = email,
                    password = password,
                    age = 0,
                    height = 0f,
                    weight = 0f,
                    role = role.lowercase(),
                    joinDate = date
                )
                userRepository.insertUser(newUser)
                _currentUser.value = newUser
                _isLoggedIn.value = true
                saveSession(newUser)
                Log.d("AuthViewModel", "Registration successful for user: $email with role: ${newUser.role}")
                onSuccess()
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Registration error", e)
                onError(e.message ?: "Registration failed")
            }
        }
    }

    fun login(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val user = userRepository.getUserByEmail(email)
                if (user != null && user.password == password) {
                    _currentUser.value = user
                    _isLoggedIn.value = true
                    saveSession(user)
                    Log.d("AuthViewModel", "Login successful for user: $email with role: ${user.role}")
                    onSuccess()
                } else {
                    onError("Invalid email or password")
                    Log.d("AuthViewModel", "Login failed: Invalid credentials")
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Login error", e)
                onError(e.message ?: "Login failed")
            }
        }
    }

    fun logout() {
        clearSession()
        _currentUser.value = null
        _isLoggedIn.value = false
        _loginError.value = null
        _registerError.value = null
        Log.d("AuthViewModel", "User logged out and session cleared")
    }

    private fun calculateBMI(height: Float, weight: Float): Float {
        val heightInMeters = height / 100f
        return (weight / (heightInMeters * heightInMeters) * 10).toInt() / 10f
    }

    private fun saveSession(user: UserEntity) {
        if (user.role.lowercase() != "admin") {
            sharedPreferences.edit().apply {
                putString("user_email", user.email)
                putString("user_role", user.role.lowercase())
                apply()
            }
            Log.d("AuthViewModel", "Session saved for user: ${user.email} with role: ${user.role}")
        } else {
            Log.d("AuthViewModel", "Admin user detected, not saving session")
        }
    }

    private fun clearSession() {
        sharedPreferences.edit().clear().apply()
        _currentUser.value = null
        _isLoggedIn.value = false
        Log.d("AuthViewModel", "Session cleared")
    }

    companion object {
        fun provideFactory(app: GymManagementApp): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AuthViewModel(app, app.userRepository) as T
            }
        }
    }
}
