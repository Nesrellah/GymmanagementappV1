package com.example.gymmanagementapp.viewmodels

// AuthViewModel.kt
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.gymmanagementapp.data.database.AppDatabase
import com.example.gymmanagementapp.data.entities.User
import com.example.gymmanagementapp.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = UserRepository(
        AppDatabase.getDatabase(application).userDao()
    )

    // Authentication states
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        data class Success(val isAdmin: Boolean) : LoginState()
        data class Error(val message: String) : LoginState()
    }

    fun registerUser(user: User) {
        viewModelScope.launch {
            try {
                userRepository.registerUser(user)
                _loginState.value = LoginState.Success(isAdmin = user.isAdmin)
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Registration failed")
            }
        }
    }

    fun login(email: String, password: String, navController: NavController) {
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            try {
                val user = userRepository.login(email, password)
                if (user != null) {
                    _loginState.value = LoginState.Success(isAdmin = user.isAdmin)
                } else {
                    _loginState.value = LoginState.Error("Invalid credentials")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Login failed")
            }
        }
    }
}