package com.example.gymmanagement.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymmanagement.data.model.UserProfile
import com.example.gymmanagement.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminMemberViewModel(
    private val repository: UserRepository
) : ViewModel() {
    private val _members = MutableStateFlow<List<UserProfile>>(emptyList())
    val members: StateFlow<List<UserProfile>> = _members

    init {
        loadMembers()
    }

    private fun loadMembers() {
        viewModelScope.launch {
            repository.getAllUserProfiles().collect { profiles ->
                _members.value = profiles.filter { it.role == "MEMBER" }
            }
        }
    }

    fun addMember(
        email: String,
        name: String,
        phone: String? = null,
        address: String? = null
    ) {
        viewModelScope.launch {
            val profile = UserProfile(
                id = 0,
                email = email,
                name = name,
                phone = phone,
                address = address,
                role = "MEMBER"
            )
            repository.insertUserProfile(profile)
        }
    }

    fun updateMember(profile: UserProfile) {
        viewModelScope.launch {
            repository.updateUserProfile(profile)
        }
    }

    fun deleteMember(profile: UserProfile) {
        viewModelScope.launch {
            repository.deleteUserProfile(profile)
        }
    }
} 