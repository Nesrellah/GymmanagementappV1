package com.example.gymmanagement.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymmanagement.data.model.UserProfile
import com.example.gymmanagement.data.repository.UserRepository
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
                _members.value = profiles
            }
        }
    }

    fun deleteMember(profile: UserProfile) {
        viewModelScope.launch {
            repository.deleteUserProfile(profile)
            repository.getUserByEmail(profile.email)?.let { userEntity ->
                repository.deleteUser(userEntity)
            }
        }
    }
} 