package com.example.gymmanagement.ui.screens.admin.workout

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.gymmanagement.data.database.AppDatabase
import com.example.gymmanagement.data.model.Workout
import com.example.gymmanagement.data.repository.WorkoutRepositoryImpl
import com.example.gymmanagement.viewmodel.AdminWorkoutViewModel
import com.example.gymmanagement.utils.ImagePicker
import com.example.gymmanagement.utils.rememberImagePicker

private val PrimaryBlue = Color(0xFF0000FF)
private val BackgroundGray = Color(0xFFF5F5F5)
private val CardBlue = Color(0xFFE6E9FD)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminWorkoutScreen(
    viewModel: AdminWorkoutViewModel
) {
    val context = LocalContext.current
    val workouts by viewModel.workouts.collectAsState(initial = emptyList())
    val imagePath by viewModel.imagePath.collectAsState()
    
    val imagePicker = rememberImagePicker { uri ->
        viewModel.setImagePath(uri.toString())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .padding(16.dp)
    ) {
        Text(
            text = "Add Workout",
            style = MaterialTheme.typography.titleMedium,
            color = PrimaryBlue,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clickable { imagePicker.launch("image/*") },
            colors = CardDefaults.cardColors(containerColor = CardBlue)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (imagePath != null) {
                    AsyncImage(
                        model = imagePath,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Add Image",
                            tint = PrimaryBlue,
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = "Tap to add an image",
                            color = PrimaryBlue,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        var eventTitle by remember { mutableStateOf("") }
        var traineeId by remember { mutableStateOf("") }
        var sets by remember { mutableStateOf("") }
        var repsOrSecs by remember { mutableStateOf("") }
        var restTime by remember { mutableStateOf("") }

        OutlinedTextField(
            value = eventTitle,
            onValueChange = { eventTitle = it },
            label = { Text("Event title") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryBlue,
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = traineeId,
            onValueChange = { traineeId = it },
            label = { Text("Trainee Id") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryBlue,
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = sets,
            onValueChange = { sets = it },
            label = { Text("Sets") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryBlue,
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = repsOrSecs,
            onValueChange = { repsOrSecs = it },
            label = { Text("Reps/sec") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryBlue,
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = restTime,
            onValueChange = { restTime = it },
            label = { Text("Rest time") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryBlue,
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val workout = Workout(
                    id = 0,
                    eventTitle = eventTitle,
                    traineeId = traineeId,
                    sets = sets.toIntOrNull() ?: 0,
                    repsOrSecs = repsOrSecs.toIntOrNull() ?: 0,
                    restTime = restTime.toIntOrNull() ?: 0,
                    imageUri = imagePath
                )
                viewModel.insertWorkout(workout)
                // Reset form
                eventTitle = ""
                traineeId = ""
                sets = ""
                repsOrSecs = ""
                restTime = ""
                viewModel.setImagePath(null)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text("Create")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(workouts) { workout ->
                WorkoutCard(
                    workout = workout,
                    onEdit = { viewModel.updateWorkout(workout) },
                    onDelete = { viewModel.deleteWorkout(workout) }
                )
            }
        }
    }
}

@Composable
fun WorkoutCard(
    workout: Workout,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AsyncImage(
                model = workout.imageUri,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = workout.eventTitle,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "${workout.sets} sets | ${workout.repsOrSecs} reps/sec",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Text(
                    text = "Rest: ${workout.restTime} sec",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}

