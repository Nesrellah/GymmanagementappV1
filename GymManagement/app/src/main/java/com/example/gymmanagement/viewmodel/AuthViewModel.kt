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
            val profile = repository.getCurrentUser()
            if (profile != null) {
                _currentUser.value = profile
                _isLoggedIn.value = true
            } else {
                _currentUser.value = null
                _isLoggedIn.value = false
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

    fun registerUser(
        name: String, email: String, password: String,
        age: String, height: String, weight: String,
        role: String = "admin",
        onComplete: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            val nameError = validateName(name)
            val emailError = validateEmail(email)
            val passwordError = validatePassword(password)
            val ageError = validateAge(age)
            val heightError = validateHeight(height)
            val weightError = validateWeight(weight)

            if (nameError != null || emailError != null || passwordError != null ||
                ageError != null || heightError != null || weightError != null) {
                val errorMessages = listOfNotNull(
                    nameError, emailError, passwordError,
                    ageError, heightError, weightError
                )
                _registerError.value = "Please fix the following issues:\n${errorMessages.joinToString("\n")}"
                onComplete(false, "Registration failed. Please check the form for errors.")
                return@launch
            }

            val existingUser = repository.getUserByEmail(email)
            if (existingUser != null) {
                _registerError.value = "This email address is already registered. Please use a different email or try logging in."
                onComplete(false, "Email already exists")
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
            onComplete(true, "Registration successful! Welcome to our fitness community, $name!")
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val emailError = validateEmail(email)
            val passwordError = validatePassword(password)

            if (emailError != null || passwordError != null) {
                val errorMessages = listOfNotNull(emailError, passwordError)
                _loginError.value = "Please fix the following issues:\n${errorMessages.joinToString("\n")}"
                return@launch
            }

            repository.login(email, password)?.let { user ->
                repository.getUserProfileByEmail(email)?.let { profile ->
                    _currentUser.value = profile
                    _isLoggedIn.value = true
                    _loginError.value = null
                    repository.saveCurrentUser(profile)
                }
            } ?: run {
                _loginError.value = "Invalid email or password. Please check your credentials and try again."
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
