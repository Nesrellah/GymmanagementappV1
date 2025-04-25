package com.example.gymmanagement.ui.screens.admin.workout

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gymmanagement.data.database.AppDatabase
import com.example.gymmanagement.data.model.Workout
import com.example.gymmanagement.data.repository.WorkoutRepository
import com.example.gymmanagement.data.repository.WorkoutRepositoryImpl
import com.example.gymmanagement.viewmodel.AdminWorkoutViewModel
import com.example.gymmanagement.utils.ImagePicker
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
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val workoutRepository = WorkoutRepositoryImpl(database.workoutDao())
    
    val viewModel: AdminWorkoutViewModel = viewModel(
        factory = AdminWorkoutViewModel.Factory(workoutRepository, ImagePicker(context))
    )
    
    val imagePicker = rememberImagePicker { uri ->
        // Save image to internal storage using ImagePicker
        val imagePickerInstance = ImagePicker(context)
        val savedPath = imagePickerInstance.saveImageToInternalStorage(uri)
        // Update the ViewModel with the saved image path
        viewModel.setImagePath(savedPath)
    }
    
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedWorkout by remember { mutableStateOf<Workout?>(null) }
    val workouts by viewModel.workouts.collectAsState(initial = emptyList())
    val imagePath by viewModel.imagePath.collectAsState()

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
                    viewModel.insertWorkout(workout)
                },
                viewModel = viewModel,
                onImageSelected = { imagePicker.launch("image/*") }
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(workouts) { workout ->
                    WorkoutCard(
                        workout = workout,
                        onEdit = {
                            selectedWorkout = workout
                            showEditDialog = true
                        },
                        onDelete = {
                            viewModel.deleteWorkout(workout)
                        }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        WorkoutDialog(
            onDismiss = { showAddDialog = false },
            onSave = { eventTitle, traineeId, sets, repsOrSecs, restTime, imageUri ->
                val workout = Workout(
                    id = 0,
                    eventTitle = eventTitle,
                    traineeId = traineeId,
                    sets = sets,
                    repsOrSecs = repsOrSecs,
                    restTime = restTime,
                    imageUri = imagePath
                )
                viewModel.insertWorkout(workout)
                showAddDialog = false
            }
        )
    }

    if (showEditDialog && selectedWorkout != null) {
        WorkoutDialog(
            workout = selectedWorkout,
            onDismiss = { showEditDialog = false },
            onSave = { eventTitle, traineeId, sets, repsOrSecs, restTime, imageUri ->
                val updatedWorkout = selectedWorkout!!.copy(
                    eventTitle = eventTitle,
                    traineeId = traineeId,
                    sets = sets,
                    repsOrSecs = repsOrSecs,
                    restTime = restTime,
                    imageUri = imagePath ?: selectedWorkout!!.imageUri
                )
                viewModel.updateWorkout(updatedWorkout)
                showEditDialog = false
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutForm(
    onWorkoutCreated: (Workout) -> Unit,
    viewModel: AdminWorkoutViewModel,
    onImageSelected: () -> Unit
) {
    var eventTitle by remember { mutableStateOf("") }
    var traineeId by remember { mutableStateOf("") }
    var sets by remember { mutableStateOf("") }
    var repsOrSecs by remember { mutableStateOf("") }
    var restTime by remember { mutableStateOf("") }
    val imagePath by viewModel.imagePath.collectAsState()

    Column(modifier = Modifier.fillMaxWidth()) {
        // Image picker card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clickable(onClick = onImageSelected),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = LightBlue)
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
            label = { Text("Workout title", fontSize = 12.sp) },
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
            label = { Text("Reps/Secs", fontSize = 12.sp) },
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
            label = { Text("Rest time (seconds)", fontSize = 12.sp) },
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
                    id = 0,
                    eventTitle = eventTitle,
                    traineeId = traineeId,
                    sets = sets.toIntOrNull() ?: 0,
                    repsOrSecs = repsOrSecs.toIntOrNull() ?: 0,
                    restTime = restTime.toIntOrNull() ?: 0,
                    imageUri = imagePath
                )
                onWorkoutCreated(workout)
                // Reset form
                eventTitle = ""
                traineeId = ""
                sets = ""
                repsOrSecs = ""
                restTime = ""
                viewModel.setImagePath(null)
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


@Composable
fun WorkoutCard(
    workout: Workout,
    onEdit: () -> Unit,
    onDelete: () -> Unit
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
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(workout.imageUri)
                        .crossfade(true)
                        .build(),
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
                            onClick = onEdit,
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
                            onClick = onDelete,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutDialog(
    workout: Workout? = null,
    onDismiss: () -> Unit,
    onSave: (String, String, Int, Int, Int, Uri?) -> Unit
) {
    var eventTitle by remember { mutableStateOf(workout?.eventTitle ?: "") }
    var traineeId by remember { mutableStateOf(workout?.traineeId ?: "") }
    var sets by remember { mutableStateOf(workout?.sets?.toString() ?: "") }
    var repsOrSecs by remember { mutableStateOf(workout?.repsOrSecs?.toString() ?: "") }
    var restTime by remember { mutableStateOf(workout?.restTime?.toString() ?: "") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (workout == null) "Add Workout" else "Edit Workout") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Image Selection
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFE1EBF5))
                        .clickable { imagePickerLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (imageUri != null) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(imageUri)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Selected Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text("Click to select image")
                    }
                }

                // Input Fields
                OutlinedTextField(
                    value = eventTitle,
                    onValueChange = { eventTitle = it },
                    label = { Text("Workout Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = traineeId,
                    onValueChange = { traineeId = it },
                    label = { Text("Trainee ID") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = sets,
                    onValueChange = { sets = it },
                    label = { Text("Sets") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = repsOrSecs,
                    onValueChange = { repsOrSecs = it },
                    label = { Text("Reps/Secs") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = restTime,
                    onValueChange = { restTime = it },
                    label = { Text("Rest Time (seconds)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        eventTitle,
                        traineeId,
                        sets.toIntOrNull() ?: 0,
                        repsOrSecs.toIntOrNull() ?: 0,
                        restTime.toIntOrNull() ?: 0,
                        imageUri
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3700B3)
                )
            ) {
                Text(if (workout == null) "Create" else "Update")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

