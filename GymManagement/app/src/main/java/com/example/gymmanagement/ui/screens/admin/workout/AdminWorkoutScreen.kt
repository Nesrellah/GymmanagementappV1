package com.example.gymmanagement.ui.screens.admin.workout

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.gymmanagement.data.model.Workout
import com.example.gymmanagement.viewmodel.AdminWorkoutViewModel
import com.example.gymmanagement.utils.rememberImagePicker
import kotlinx.coroutines.flow.Flow
import com.example.gymmanagement.ui.theme.GymManagementAppTheme


// Define custom colors
private val DeepBlue = Color(0xFF0000CD)
private val LightBlue = Color(0xFFE6E9FD)
private val ImagePickerBlue = Color(0xFFCFD4FE)
private val White = Color.White
private val Green = Color(0xFF4CAF50)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminWorkoutScreen(
    onNavigateToEvents: () -> Unit,
    onNavigateToProgress: () -> Unit
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedWorkout by remember { mutableStateOf<Workout?>(null) }
    val viewModel: AdminWorkoutViewModel = viewModel()
    val workouts by viewModel.workouts.collectAsState(initial = emptyList())

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
                text = "Workouts",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBlue
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = onNavigateToEvents,
                    modifier = Modifier
                        .background(LightBlue, CircleShape)
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Events",
                        tint = DeepBlue
                    )
                }
                
                IconButton(
                    onClick = onNavigateToProgress,
                    modifier = Modifier
                        .background(LightBlue, CircleShape)
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Progress",
                        tint = DeepBlue
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Add Workout",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            WorkoutForm(
                onWorkoutCreated = { workout ->
                    viewModel.addWorkout(workout)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(workouts) { workout ->
                    WorkoutCard(
                        workout = workout,
                        onEditClick = {
                            selectedWorkout = workout
                            showEditDialog = true
                        },
                        onDeleteClick = {
                            viewModel.deleteWorkout(workout)
                        }
                    )
                }
            }
        }
    }

    if (showEditDialog) {
        EditWorkoutDialog(
            workout = selectedWorkout,
            onDismissRequest = { showEditDialog = false },
            onConfirm = { updatedWorkout ->
                viewModel.updateWorkout(updatedWorkout)
                showEditDialog = false
            }
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun AdminWorkoutScreenPreview() {
    GymManagementAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AdminWorkoutScreen({}, {})
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutForm(
    onWorkoutCreated: (Workout) -> Unit
) {
    var eventTitle by remember { mutableStateOf("") }
    var traineeId by remember { mutableStateOf("") }
    var sets by remember { mutableStateOf("") }
    var repsOrSecs by remember { mutableStateOf("") }
    var restTime by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    
    val imagePicker = rememberImagePicker { uri ->
        imageUri = uri
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Image picker card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clickable { imagePicker.launch("image/*") },
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = LightBlue)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Add Image",
                            tint = DeepBlue,
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = "Tap to add an image",
                            color = DeepBlue,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = eventTitle,
            onValueChange = { eventTitle = it },
            label = { Text("Event title", fontSize = 12.sp) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = LightBlue,
                focusedBorderColor = DeepBlue
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = traineeId,
            onValueChange = { traineeId = it },
            label = { Text("Trainee Id", fontSize = 12.sp) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = LightBlue,
                focusedBorderColor = DeepBlue
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = sets,
            onValueChange = { sets = it },
            label = { Text("Sets", fontSize = 12.sp) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = LightBlue,
                focusedBorderColor = DeepBlue
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = repsOrSecs,
            onValueChange = { repsOrSecs = it },
            label = { Text("Reps/ sec", fontSize = 12.sp) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = LightBlue,
                focusedBorderColor = DeepBlue
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = restTime,
            onValueChange = { restTime = it },
            label = { Text("Rest time", fontSize = 12.sp) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                val workout = Workout(
                    eventTitle = eventTitle,
                    traineeId = traineeId,
                    sets = sets.toIntOrNull() ?: 0,
                    repsOrSecs = repsOrSecs.toIntOrNull() ?: 0,
                    restTime = restTime.toIntOrNull() ?: 0,
                    imageUri = imageUri?.toString()
                )
                onWorkoutCreated(workout)
                // Reset form
                eventTitle = ""
                traineeId = ""
                sets = ""
                repsOrSecs = ""
                restTime = ""
                imageUri = null
            },
            enabled = eventTitle.isNotBlank() && traineeId.isNotBlank() &&
                    sets.isNotBlank() && repsOrSecs.isNotBlank() && restTime.isNotBlank(),
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
                text = "Create",
                fontSize = 14.sp,
                maxLines = 1
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun WorkoutFormPreview() {
    GymManagementAppTheme {
        Surface(
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            WorkoutForm(onWorkoutCreated = {})
        }
    }
}

@Composable
fun WorkoutCard(
    workout: Workout,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box {
            if (workout.imageUri != null) {
                AsyncImage(
                    model = workout.imageUri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Surface(
                        color = Color.White.copy(alpha = 0.9f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "Trainee ID: ${workout.traineeId}",
                            color = Color.Black,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        IconButton(
                            onClick = onEditClick,
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color.White, RoundedCornerShape(4.dp))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = Color.Black,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = onDeleteClick,
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color.White, RoundedCornerShape(4.dp))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.Black,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Surface(
                        color = Color.White.copy(alpha = 0.9f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = workout.eventTitle,
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Surface(
                        color = Color.White.copy(alpha = 0.9f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "${workout.sets} sets | ${workout.repsOrSecs} reps | ${workout.restTime}s rest",
                            color = Color.Black,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun WorkoutCardPreview() {
    GymManagementAppTheme {
        Surface(
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            WorkoutCard(
                workout = Workout(
                    id = 1,
                    eventTitle = "Push ups",
                    traineeId = "TR001",
                    sets = 3,
                    repsOrSecs = 15,
                    restTime = 30,
                    imageUri = null
                ),
                onEditClick = {},
                onDeleteClick = {}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditWorkoutDialog(
    workout: Workout?,
    onDismissRequest: () -> Unit,
    onConfirm: (Workout) -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(8.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Edit Workout",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                var eventTitle by remember { mutableStateOf(workout?.eventTitle ?: "") }
                var traineeId by remember { mutableStateOf(workout?.traineeId ?: "") }
                var sets by remember { mutableStateOf(workout?.sets?.toString() ?: "") }
                var repsOrSecs by remember { mutableStateOf(workout?.repsOrSecs?.toString() ?: "") }
                var restTime by remember { mutableStateOf(workout?.restTime?.toString() ?: "") }
                var imageUri by remember { mutableStateOf<Uri?>(workout?.imageUri?.let { Uri.parse(it) }) }

                val imagePicker = rememberImagePicker { uri ->
                    imageUri = uri
                }

                // Image picker
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clickable { imagePicker.launch("image/*") },
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = LightBlue)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (imageUri != null) {
                            AsyncImage(
                                model = imageUri,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Add Image",
                                    tint = DeepBlue,
                                    modifier = Modifier.size(32.dp)
                                )
                                Text(
                                    text = "Tap to add an image",
                                    color = DeepBlue,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Form fields
                OutlinedTextField(
                    value = eventTitle,
                    onValueChange = { eventTitle = it },
                    label = { Text("Event title", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = LightBlue,
                        focusedBorderColor = DeepBlue
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = traineeId,
                    onValueChange = { traineeId = it },
                    label = { Text("Trainee Id", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = LightBlue,
                        focusedBorderColor = DeepBlue
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = sets,
                    onValueChange = { sets = it },
                    label = { Text("Sets", fontSize = 12.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = LightBlue,
                        focusedBorderColor = DeepBlue
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = repsOrSecs,
                    onValueChange = { repsOrSecs = it },
                    label = { Text("Reps/ sec", fontSize = 12.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = LightBlue,
                        focusedBorderColor = DeepBlue
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = restTime,
                    onValueChange = { restTime = it },
                    label = { Text("Rest time", fontSize = 12.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = LightBlue,
                        focusedBorderColor = DeepBlue
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = onDismissRequest,
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Cancel", fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            workout?.id?.let { id ->
                                onConfirm(
                                    Workout(
                                        id = id,
                                        eventTitle = eventTitle,
                                        traineeId = traineeId,
                                        sets = sets.toIntOrNull() ?: 0,
                                        repsOrSecs = repsOrSecs.toIntOrNull() ?: 0,
                                        restTime = restTime.toIntOrNull() ?: 0,
                                        imageUri = imageUri?.toString()
                                    )
                                )
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text("Delete", fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            workout?.id?.let { id ->
                                onConfirm(
                                    Workout(
                                        id = id,
                                        eventTitle = eventTitle,
                                        traineeId = traineeId,
                                        sets = sets.toIntOrNull() ?: 0,
                                        repsOrSecs = repsOrSecs.toIntOrNull() ?: 0,
                                        restTime = restTime.toIntOrNull() ?: 0,
                                        imageUri = imageUri?.toString()
                                    )
                                )
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DeepBlue,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text("Update", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun EditWorkoutDialogPreview() {
    GymManagementAppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        ) {
            EditWorkoutDialog(
                workout = Workout(
                    id = 1,
                    eventTitle = "Push ups",
                    traineeId = "TR001",
                    sets = 3,
                    repsOrSecs = 15,
                    restTime = 30,
                    imageUri = null
                ),
                onDismissRequest = {},
                onConfirm = {}
            )
        }
    }
} 