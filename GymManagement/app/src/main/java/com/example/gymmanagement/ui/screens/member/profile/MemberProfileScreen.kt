package com.example.gymmanagement.ui.screens.member.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymmanagement.data.model.UserProfile
import com.example.gymmanagement.viewmodel.MemberProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberProfileScreen(
    traineeId: String,
    viewModel: MemberProfileViewModel = viewModel()
) {
    var isEditing by remember { mutableStateOf(false) }
    val userProfile by viewModel.userProfile.collectAsState()

    LaunchedEffect(traineeId) {
        viewModel.getUserProfile(traineeId.toInt())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top App Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0000CD))
                .padding(16.dp)
        ) {
            Text(
                text = "Your profile",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }

        userProfile?.let { profile ->
            if (!isEditing) {
                DisplayProfile(
                    profile = profile,
                    onEditClick = { isEditing = true }
                )
            } else {
                EditProfile(
                    profile = profile,
                    onSave = { updatedProfile ->
                        viewModel.updateUserProfileWithBMI(
                            email = updatedProfile.email,
                            name = updatedProfile.name,
                            age = updatedProfile.age,
                            height = updatedProfile.height,
                            weight = updatedProfile.weight,
                            phone = updatedProfile.phone,
                            address = updatedProfile.address,
                            role = updatedProfile.role
                        )
                        isEditing = false
                    },
                    onCancel = { isEditing = false }
                )
            }
        }
    }
}

@Composable
fun DisplayProfile(
    profile: UserProfile,
    onEditClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        // Profile Icon
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0000CD)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        // Personal Information Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Personal information",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    IconButton(onClick = onEditClick) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = Color(0xFF0000CD)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                ProfileField("Name:", profile.name)
                ProfileField("Email:", profile.email)
                profile.age?.let { ProfileField("Age:", "$it years") }
                profile.height?.let { ProfileField("Height:", "$it cm") }
                profile.weight?.let { ProfileField("Weight:", "$it kg") }
                profile.bmi?.let { ProfileField("BMI:", String.format("%.1f", it)) }
                profile.joinDate?.let { ProfileField("Join Date:", it) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfile(
    profile: UserProfile,
    onSave: (UserProfile) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf(profile.name) }
    var age by remember { mutableStateOf(profile.age?.toString() ?: "") }
    var height by remember { mutableStateOf(profile.height?.toString() ?: "") }
    var weight by remember { mutableStateOf(profile.weight?.toString() ?: "") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Personal Information",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(4.dp)
                )

                TextField(
                    value = age,
                    onValueChange = { age = it },
                    label = { Text("Age") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(4.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                TextField(
                    value = height,
                    onValueChange = { height = it },
                    label = { Text("Height(cm)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(4.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                TextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("Weight(kg)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(4.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onCancel,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(4.dp),
                        border = BorderStroke(1.dp, Color.Gray)
                    ) {
                        Text(
                            "Cancel",
                            color = Color.Black
                        )
                    }
                    Button(
                        onClick = {
                            val updatedProfile = profile.copy(
                                name = name,
                                age = age.toIntOrNull(),
                                height = height.toFloatOrNull(),
                                weight = weight.toFloatOrNull()
                            )
                            onSave(updatedProfile)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0000CD)
                        ),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileField(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 14.sp
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun calculateBMI(height: Float?, weight: Float?): Float? {
    if (height == null || weight == null || height <= 0) return null
    val heightInMeters = height / 100
    return weight / (heightInMeters * heightInMeters)
}