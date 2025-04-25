package com.example.gymmanagement.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
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
    private val repository: UserRepository
) : ViewModel() {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _currentUser = MutableStateFlow<UserProfile?>(null)
    val currentUser: StateFlow<UserProfile?> = _currentUser

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError

    private val _registerError = MutableStateFlow<String?>(null)
    val registerError: StateFlow<String?> = _registerError

    init {
        checkLoginState()
    }

    fun checkLoginState() {
        viewModelScope.launch {
            repository.getCurrentUser()?.let { profile ->
                _currentUser.value = profile
                _isLoggedIn.value = true
            }
        }
    }

    fun validateEmail(email: String): String? {
        if (email.isEmpty()) return "Email is required"
        val emailPattern = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@(.+)\$"
        )
        if (!emailPattern.matcher(email).matches()) {
            return "Please enter a valid email address"
        }
        return null
    }

    fun validatePassword(password: String): String? {
        if (password.isEmpty()) return "Password is required"
        if (password.length < 6) return "Password must be at least 6 characters"
        return null
    }

    fun validateName(name: String): String? {
        if (name.isEmpty()) return "Name is required"
        if (name.length < 2) return "Name must be at least 2 characters"
        return null
    }

    fun validateAge(age: String): String? {
        if (age.isEmpty()) return "Age is required"
        val ageInt = age.toIntOrNull()
        if (ageInt == null) return "Please enter a valid age"
        if (ageInt < 13 || ageInt > 100) return "Age must be between 13 and 100"
        return null
    }

    fun validateHeight(height: String): String? {
        if (height.isEmpty()) return "Height is required"
        val heightFloat = height.toFloatOrNull()
        if (heightFloat == null) return "Please enter a valid height"
        if (heightFloat < 100 || heightFloat > 250) return "Height must be between 100cm and 250cm"
        return null
    }

    fun validateWeight(weight: String): String? {
        if (weight.isEmpty()) return "Weight is required"
        val weightFloat = weight.toFloatOrNull()
        if (weightFloat == null) return "Please enter a valid weight"
        if (weightFloat < 30 || weightFloat > 300) return "Weight must be between 30kg and 300kg"
        return null
    }

    fun registerUser(
        name: String, email: String, password: String,
        age: String, height: String, weight: String,
        role: String = "member",
        onComplete: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            // Validate all fields
            val nameError = validateName(name)
            val emailError = validateEmail(email)
            val passwordError = validatePassword(password)
            val ageError = validateAge(age)
            val heightError = validateHeight(height)
            val weightError = validateWeight(weight)

            if (nameError != null || emailError != null || passwordError != null ||
                ageError != null || heightError != null || weightError != null) {
                _registerError.value = listOfNotNull(
                    nameError, emailError, passwordError,
                    ageError, heightError, weightError
                ).joinToString("\n")
                onComplete(false, "Please fix the validation errors")
                return@launch
            }

            val existingUser = repository.getUserByEmail(email)
            if (existingUser != null) {
                _registerError.value = "User with this email already exists"
                onComplete(false, "User already exists")
                return@launch
            }

            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            val newUser = UserEntity(
                id = 0,
                name = name,
                email = email,
                password = password,
                age = age.toInt(),
                height = height.toFloat(),
                weight = weight.toFloat(),
                role = role,
                joinDate = date
            )

            repository.insertUser(newUser)

            val userProfile = UserProfile(
                id = 0,
                email = email,
                name = name,
                age = age.toInt(),
                height = height.toFloat(),
                weight = weight.toFloat(),
                bmi = calculateBMI(height.toFloat(), weight.toFloat()),
                joinDate = date,
                role = role
            )

            repository.insertUserProfile(userProfile)
            _registerError.value = null
            onComplete(true, "Registered successfully")
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val emailError = validateEmail(email)
            val passwordError = validatePassword(password)

            if (emailError != null || passwordError != null) {
                _loginError.value = listOfNotNull(emailError, passwordError).joinToString("\n")
                return@launch
            }

            repository.login(email, password)?.let { user ->
                repository.getUserProfileByEmail(email)?.let { profile ->
                    _currentUser.value = profile
                    _isLoggedIn.value = true
                    _loginError.value = null
                    // Save user session
                    repository.saveCurrentUser(profile)
                }
            } ?: run {
                _loginError.value = "Invalid email or password"
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
        viewModelScope.launch {
            repository.clearCurrentUser()
            _isLoggedIn.value = false
            _currentUser.value = null
            _loginError.value = null
            _registerError.value = null
        }
    }

    private fun calculateBMI(height: Float, weight: Float): Float {
        val heightInMeters = height / 100f
        return (weight / (heightInMeters * heightInMeters) * 10).toInt() / 10f
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
