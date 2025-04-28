package com.example.gymmanagement.ui.screens.admin.member

import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymmanagement.data.model.UserEntity
import com.example.gymmanagement.data.model.UserProfile
import com.example.gymmanagement.data.repository.UserRepository
import com.example.gymmanagement.data.repository.UserRepositoryImpl
import com.example.gymmanagement.ui.theme.GymManagementAppTheme
import com.example.gymmanagement.viewmodel.AdminMemberViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

private val DeepBlue = Color(0xFF0000CD)
private val LightBlue = Color(0xFFE6E9FD)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminMemberScreen(
    viewModel: AdminMemberViewModel
) {
    val members by viewModel.members.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top Navigation Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Members",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBlue
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Add Member",
                style = MaterialTheme.typography.titleMedium.copy(color = Color.Blue),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            MemberForm(
                onMemberCreated = { profile ->
                    viewModel.addMember(
                        email = profile.email,
                        name = profile.name,
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "All Members",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            
            Text(
                text = "Manage your gym members",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(members) { member ->
                    MemberCard(
                        member = member,
                        onEditClick = {
                            // TODO: Implement edit functionality
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberForm(
    onMemberCreated: (UserProfile) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", fontSize = 12.sp) },
            placeholder = { Text("Enter email", fontSize = 12.sp) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = LightBlue,
                focusedBorderColor = DeepBlue
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name", fontSize = 12.sp) },
            placeholder = { Text("Enter name", fontSize = 12.sp) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = LightBlue,
                focusedBorderColor = DeepBlue
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone", fontSize = 12.sp) },
            placeholder = { Text("Enter phone", fontSize = 12.sp) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = LightBlue,
                focusedBorderColor = DeepBlue
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Address", fontSize = 12.sp) },
            placeholder = { Text("Enter address", fontSize = 12.sp) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = LightBlue,
                focusedBorderColor = DeepBlue
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val profile = UserProfile(
                    id = 0,
                    email = email,
                    name = name,
                    role = "MEMBER"
                )
                onMemberCreated(profile)
                // Reset form
                email = ""
                name = ""
                phone = ""
                address = ""
            },
            enabled = email.isNotBlank() && name.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DeepBlue,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                text = "Add Member",
                fontSize = 14.sp,
                maxLines = 1
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberCard(
    member: UserProfile,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
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
            Text(
                text = "Role: ${member.role}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

