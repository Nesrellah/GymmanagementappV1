package com.example.gymmanagement.ui.screens.member.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymmanagement.data.model.UserProfile
import com.example.gymmanagement.ui.theme.GymManagementAppTheme
import com.example.gymmanagement.viewmodel.UserProfileViewModel
import com.example.gymmanagement.viewmodel.MemberProfileViewModel

@Composable
fun MemberProfileScreen(
    traineeId: String,
    viewModel: MemberProfileViewModel = viewModel()
) {
    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var showEditDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(traineeId) {
        viewModel.getUserProfile(traineeId.toInt())?.let { profile ->
            userProfile = profile
        }
        isLoading = false
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            userProfile?.let { profile ->
                DisplayProfileContent(profile)
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = { showEditDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Edit Profile")
                }
            } ?: run {
                Text(
                    text = "No profile found",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
    
    if (showEditDialog) {
        userProfile?.let { profile ->
            AlertDialog(
                onDismissRequest = { showEditDialog = false },
                title = { Text("Edit Profile") },
                text = {
                    EditProfileContent(
                        userProfile = profile,
                        onSave = { name, email, phone, address, role ->
                            viewModel.updateUserProfile(
                                email = email,
                                name = name,
                                phone = phone,
                                address = address,
                                role = role
                            )
                            showEditDialog = false
                        },
                        onCancel = { showEditDialog = false }
                    )
                },
                confirmButton = {},
                dismissButton = {}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileContent(
    userProfile: UserProfile,
    onSave: (String, String, String?, String?, String) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf(userProfile.name) }
    var email by remember { mutableStateOf(userProfile.email) }
    var phone by remember { mutableStateOf(userProfile.phone ?: "") }
    var address by remember { mutableStateOf(userProfile.address ?: "") }
    var role by remember { mutableStateOf(userProfile.role) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Address") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text("Cancel")
            }

            Button(
                onClick = {
                    if (name.isNotBlank() && email.isNotBlank()) {
                        onSave(
                            name,
                            email,
                            phone.takeIf { it.isNotBlank() },
                            address.takeIf { it.isNotBlank() },
                            role
                        )
                    }
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3700B3)
                ),
                enabled = name.isNotBlank() && email.isNotBlank()
            ) {
                Text("Save")
            }
        }
    }
}

@Composable
fun DisplayProfileContent(userProfile: UserProfile) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ProfileField("Name:", userProfile.name)
        ProfileField("Email:", userProfile.email)
        userProfile.age?.let { ProfileField("Age:", "$it years") }
        userProfile.height?.let { ProfileField("Height:", "$it cm") }
        userProfile.weight?.let { ProfileField("Weight:", "$it kg") }
        userProfile.bmi?.let { ProfileField("BMI:", String.format("%.1f", it)) }
        userProfile.joinDate?.let { ProfileField("Join Date:", it) }
        ProfileField("Role:", userProfile.role)
    }
}

@Composable
fun ProfileField(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 16.sp
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MemberProfileScreenPreview() {
    GymManagementAppTheme {
        val previewProfile = UserProfile(
            id = 1,
            name = "John Doe",
            email = "john@example.com",
            phone = "1234567890",
            address = "123 Main St",
            role = "member",
            age = 25,
            height = 175,
            weight = 70,
            bmi = 22.9,
            joinDate = "2024-02-10"
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            DisplayProfileContent(previewProfile)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            EditProfileContent(
                userProfile = previewProfile,
                onSave = { _, _, _, _, _ -> },
                onCancel = {}
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun DisplayProfileContentPreview() {
    GymManagementAppTheme {
        Surface(
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            DisplayProfileContent(
                userProfile = UserProfile(
                    email = "blen@example.com",
                    name = "Blen Alemu",
                    role = "member",
                    age = 28,
                    height = 170,
                    weight = 60,
                    bmi = 20.8,
                    joinDate = "2024-02-10"
                )
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun EditProfileContentPreview() {
    GymManagementAppTheme {
        Surface(
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            EditProfileContent(
                userProfile = UserProfile(
                    email = "blen@example.com",
                    name = "Blen Alemu",
                    role = "member",
                    age = 28,
                    height = 170,
                    weight = 60,
                    bmi = 20.8,
                    joinDate = "2024-02-10"
                ),
                onSave = { _, _, _, _, _ -> },
                onCancel = {}
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun ProfileFieldPreview() {
    GymManagementAppTheme {
        Surface(
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                ProfileField("Name:", "Blen Alemu")
                ProfileField("Email:", "blen@example.com")
                ProfileField("Age:", "28 years")
                ProfileField("Height:", "170 cm")
                ProfileField("Weight:", "60 kg")
                ProfileField("BMI:", "20.8")
                ProfileField("Join Date:", "2024-02-10")
                ProfileField("Role:", "member")
            }
        }
    }
} 