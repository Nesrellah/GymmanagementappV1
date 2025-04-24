package com.example.gymmanagement.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gymmanagement.data.model.UserProfile
import com.example.gymmanagement.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.math.round

class MemberProfileViewModel(
    private val repository: UserRepository
) : ViewModel() {
    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile

    suspend fun getUserProfile(id: Int): UserProfile? {
        val profile = repository.getUserProfileById(id)
        _userProfile.value = profile
        return profile
    }

    fun updateUserProfile(
        email: String,
        name: String,
        phone: String? = null,
        address: String? = null,
        role: String = "member" // Default to member
    ) {
        viewModelScope.launch {
            repository.getUserProfileByEmail(email)?.let { existingProfile ->
                val updatedProfile = existingProfile.copy(
                    name = name,
                    phone = phone,
                    address = address,
                    role = role
                )
                repository.updateUserProfile(updatedProfile)
                _userProfile.value = updatedProfile
            }
        }
    }
    
    fun updateUserProfileWithBMI(
        email: String,
        name: String,
        age: Int?,
        height: Int?,
        weight: Int?,
        phone: String? = null,
        address: String? = null,
        role: String = "member" // Default to member
    ) {
        viewModelScope.launch {
            repository.getUserProfileByEmail(email)?.let { existingProfile ->
                val bmi = if (height != null && weight != null) {
                    calculateBMI(height, weight)
                } else {
                    existingProfile.bmi
                }
                
                val updatedProfile = existingProfile.copy(
                    name = name,
                    age = age,
                    height = height,
                    weight = weight,
                    bmi = bmi,
                    phone = phone,
                    address = address,
                    role = role
                )
                repository.updateUserProfile(updatedProfile)
                _userProfile.value = updatedProfile
            }
        }
    }
    
    private fun calculateBMI(heightCm: Int, weightKg: Int): Double {
        val heightM = heightCm / 100.0
        val bmi = weightKg / (heightM.pow(2))
        return round(bmi * 10) / 10
    }
    
    class Factory(
        private val repository: UserRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MemberProfileViewModel::class.java)) {
                return MemberProfileViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
} 