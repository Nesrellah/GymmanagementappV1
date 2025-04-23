package com.example.gymmanagement.ui.screens.admin

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymmanagement.data.model.Workout
import com.example.gymmanagement.ui.components.ImagePickerButton
import com.example.gymmanagement.viewmodel.AdminWorkoutViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminWorkoutScreen(
    viewModel: AdminWorkoutViewModel,
    modifier: Modifier = Modifier,
    workoutToEdit: Workout? = null
) {
    var title by remember { mutableStateOf(workoutToEdit?.title ?: "") }
    var traineeId by remember { mutableStateOf(workoutToEdit?.traineeId ?: "") }
    var sets by remember { mutableStateOf(workoutToEdit?.sets?.toString() ?: "") }
    var repsOrSecs by remember { mutableStateOf(workoutToEdit?.repsOrSecs?.toString() ?: "") }
    var restTime by remember { mutableStateOf(workoutToEdit?.restTime?.toString() ?: "") }
    
    val imageUri = viewModel.selectedImageUri ?: workoutToEdit?.imageUri?.let { Uri.parse(it) }
    
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.setSelectedImage(it) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = if (workoutToEdit != null) "Edit Workout" else "Add Workout",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2196F3)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        ImagePickerButton(
            imageUri = imageUri?.toString(),
            onPickImage = { imagePicker.launch("image/*") }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Event title") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF2196F3),
                unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
            )
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = traineeId,
            onValueChange = { traineeId = it },
            label = { Text("Trainee Id") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF2196F3),
                unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
            )
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = sets,
            onValueChange = { sets = it },
            label = { Text("Sets") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF2196F3),
                unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
            )
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = repsOrSecs,
            onValueChange = { repsOrSecs = it },
            label = { Text("Reps/ sec") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF2196F3),
                unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
            )
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = restTime,
            onValueChange = { restTime = it },
            label = { Text("Rest time") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF2196F3),
                unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
            )
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (workoutToEdit != null) {
                // Edit mode buttons
                OutlinedButton(
                    onClick = { /* TODO: Handle cancel */ },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text("Cancel")
                }
                
                Button(
                    onClick = { /* TODO: Handle delete */ },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text("Delete")
                }
                
                Button(
                    onClick = {
                        val workout = workoutToEdit.copy(
                            title = title,
                            traineeId = traineeId,
                            sets = sets.toIntOrNull() ?: 0,
                            repsOrSecs = repsOrSecs.toIntOrNull() ?: 0,
                            restTime = restTime.toIntOrNull() ?: 0,
                            imageUri = imageUri?.toString()
                        )
                        viewModel.updateWorkout(workout)
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text("Update")
                }
            } else {
                // Add mode button
                Button(
                    onClick = {
                        val workout = Workout(
                            title = title,
                            traineeId = traineeId,
                            sets = sets.toIntOrNull() ?: 0,
                            repsOrSecs = repsOrSecs.toIntOrNull() ?: 0,
                            restTime = restTime.toIntOrNull() ?: 0,
                            imageUri = imageUri?.toString()
                        )
                        viewModel.addWorkout(workout)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text("Create")
                }
            }
        }
    }
} .git