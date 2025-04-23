package com.example.gymmanagement.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymmanagement.data.dao.MemberDao
import com.example.gymmanagement.data.model.Member
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminMemberViewModel(
    private val memberDao: MemberDao
) : ViewModel() {
    private val _members = MutableStateFlow<List<Member>>(emptyList())
    val members: StateFlow<List<Member>> = _members.asStateFlow()

    var selectedMember by mutableStateOf<Member?>(null)
        private set

    init {
        loadMembers()
    }

    private fun loadMembers() {
        viewModelScope.launch {
            memberDao.getAllMembers().collect {
                _members.value = it
            }
        }
    }

    fun searchMember(traineeId: Int) {
        viewModelScope.launch {
            selectedMember = memberDao.getMemberById(traineeId)
        }
    }

    fun clearSelectedMember() {
        selectedMember = null
    }
} 