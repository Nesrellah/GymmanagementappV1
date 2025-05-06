package com.example.gymmanagement.ui.screens.member.workout

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.gymmanagement.data.model.Workout
import com.example.gymmanagement.viewmodel.MemberWorkoutViewModel
import com.example.gymmanagement.ui.theme.GymManagementAppTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

private val DeepBlue = Color(0xFF0000CD)
private val Green = Color(0xFF4CAF50)

@Composable
fun MemberWorkoutScreen(
    viewModel: MemberWorkoutViewModel
) {
    val workouts by viewModel.workouts.collectAsState()
    val progress by viewModel.progress.collectAsState()

    // Add logging
    LaunchedEffect(workouts) {
        Log.d("MemberWorkoutScreen", "Current workouts: $workouts")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top App Bar with "Daily workout"
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1A18C6))
                .padding(vertical = 24.dp, horizontal = 16.dp)
        ) {
            Text(
                text = "Daily workout",
                color = Color.White,
                fontSize = 28.sp,
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }

        // Progress Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Today's Progress",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        style = MaterialTheme.typography.titleMedium,
                        color = DeepBlue
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = DeepBlue,
                    trackColor = Color(0xFFE0E0E0)
                )
            }
        }

        // "Your Workouts" Section
        Text(
            text = "Your Workouts",
            modifier = Modifier.padding(horizontal = 16.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = DeepBlue
        )

        if (workouts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No workouts assigned yet.\nCheck back later for your personalized workout plan!",
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        } else {
            // Workouts List
            LazyColumn(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(workouts) { workout ->
                    WorkoutCard(
                        workout = workout,
                        onToggleCompletion = { viewModel.toggleWorkoutCompletion(workout) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutCard(
    workout: Workout,
    onToggleCompletion: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Background image
            AsyncImage(
                model = workout.imageUri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            // Top left: Title in white rounded box
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
            ) {
                Surface(
                    color = Color.White.copy(alpha = 0.95f),
                    shape = RoundedCornerShape(12.dp),
                    shadowElevation = 2.dp,
                ) {
                    Text(
                        text = workout.eventTitle,
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }

            // Top right: Finish/Done overlay button
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
            ) {
                val buttonColor = if (workout.isCompleted) Green else DeepBlue
                val buttonText = if (workout.isCompleted) "Done" else "Finish"
                Surface(
                    color = buttonColor,
                    shape = RoundedCornerShape(8.dp),
                    shadowElevation = 4.dp,
                    modifier = Modifier
                        .height(36.dp)
                        .wrapContentWidth()
                        .clickable(enabled = !workout.isCompleted) { onToggleCompletion() }
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 18.dp)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            buttonText,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Bottom left: Info in a single white rounded box
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Surface(
                    color = Color.White.copy(alpha = 0.95f),
                    shape = RoundedCornerShape(12.dp),
                    shadowElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth(0.7f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Sets: ${workout.sets}",
                            color = Color.Black,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Reps/Secs: ${workout.repsOrSecs}",
                            color = Color.Black,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Rest: ${workout.restTime}s",
                            color = Color.Black,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WorkoutInfo(text: String) {
    Text(
        text = text,
        color = Color.White,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium
    )
}


