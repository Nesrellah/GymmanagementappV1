package com.example.gymmanagement.ui.screens.admin.member

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymmanagement.data.model.UserEntity
import com.example.gymmanagement.data.model.UserProfile
import com.example.gymmanagement.data.repository.UserRepository
import com.example.gymmanagement.ui.theme.GymManagementAppTheme
import com.example.gymmanagement.viewmodel.AdminMemberViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun AdminMemberScreen(
    viewModel: AdminMemberViewModel = viewModel()
) {
    val members by viewModel.members.collectAsState(initial = emptyList())
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Members",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(members) { member ->
                MemberCard(member = member)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberCard(
    member: UserProfile
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = member.name,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Email: ${member.email}",
                style = MaterialTheme.typography.bodyLarge
            )
            member.phone?.let { phone ->
                Text(
                    text = "Phone: $phone",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            member.address?.let { address ->
                Text(
                    text = "Address: $address",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Text(
                text = "Role: ${member.role}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminMemberScreenPreview() {
    val previewRepository = object : UserRepository {
        override fun getAllUsers(): Flow<List<UserEntity>> = flowOf(emptyList())
        override suspend fun getUserByEmail(email: String): UserEntity? = null
        override suspend fun login(email: String, password: String): UserEntity? = null
        override suspend fun insertUser(user: UserEntity) {}
        override suspend fun updateUser(user: UserEntity) {}
        override suspend fun deleteUser(user: UserEntity) {}
        override fun getAllUserProfiles(): Flow<List<UserProfile>> = flowOf(
            listOf(
                UserProfile(
                    id = 1,
                    name = "Abel Melaku",
                    email = "abel@example.com",
                    age = 20,
                    height = 170,
                    weight = 70,
                    bmi = 24.2,
                    joinDate = "2024-02-01",
                    role = "MEMBER"
                ),
                UserProfile(
                    id = 2,
                    name = "Meron Nebyu",
                    email = "meron@example.com",
                    age = 22,
                    height = 165,
                    weight = 55,
                    bmi = 20.1,
                    joinDate = "2024-02-05",
                    role = "MEMBER"
                )
            )
        )
        override suspend fun getUserProfileById(id: Int): UserProfile? = null
        override suspend fun getUserProfileByEmail(email: String): UserProfile? = null
        override suspend fun insertUserProfile(profile: UserProfile) {}
        override suspend fun updateUserProfile(profile: UserProfile) {}
        override suspend fun deleteUserProfile(profile: UserProfile) {}
    }

    GymManagementAppTheme {
        AdminMemberScreen(
            viewModel = AdminMemberViewModel(previewRepository)
        )
    }
} 