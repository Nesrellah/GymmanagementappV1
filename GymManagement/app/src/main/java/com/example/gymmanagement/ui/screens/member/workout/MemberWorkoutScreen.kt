package com.example.gymmanagement.ui.screens.member.workout

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymmanagement.R
import com.example.gymmanagement.data.model.MemberWorkout
import com.example.gymmanagement.data.repository.MemberWorkoutRepository
import com.example.gymmanagement.data.repository.MemberWorkoutRepositoryImpl
import com.example.gymmanagement.data.dao.MemberWorkoutDao
import com.example.gymmanagement.ui.theme.GymManagementAppTheme
import com.example.gymmanagement.viewmodel.MemberWorkoutViewModel
import kotlinx.coroutines.flow.flowOf
import androidx.compose.foundation.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

@Composable
fun MemberWorkoutScreen(
    viewModel: MemberWorkoutViewModel
) {
    val workouts by viewModel.workouts.collectAsState()
    val progress by viewModel.progress.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top App Bar with "Daily workout"
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0000CD))
                .padding(16.dp)
        ) {
            Text(
                text = "Daily workout",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
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
                        color = Color(0xFF0000CD)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = Color(0xFF0000CD),
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
            color = Color(0xFF0000CD)
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

@Composable
fun WorkoutCard(
    workout: MemberWorkout,
    onToggleCompletion: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.gym_logo),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
            )

            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Row with Title and Completion Status
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = workout.title,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (workout.isCompleted) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Completed",
                            tint = Color.Green,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Bottom Row with Workout Details
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    WorkoutInfo("${workout.sets} sets")
                    WorkoutInfo("${workout.repsOrSecs} reps")
                    WorkoutInfo("${workout.restTime}s rest")
                }
            }
        }
    }
}

@Composable
fun WorkoutInfo(text: String) {
    Surface(
        color = Color.White.copy(alpha = 0.9f),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = text,
            color = Color.Black,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun WorkoutCardPreview() {
    GymManagementAppTheme {
        WorkoutCard(
            workout = MemberWorkout(
                id = 1,
                title = "Dead Lift",
                sets = 4,
                repsOrSecs = 10,
                restTime = 60,
                imageUri = null,
                traineeId = "TR001",
                isCompleted = false
            ),
            onToggleCompletion = {}
        )
    }
}

class PreviewMemberWorkoutProvider : PreviewParameterProvider<MemberWorkoutViewModel> {
    override val values = sequenceOf(
        MemberWorkoutViewModel(
            MemberWorkoutRepositoryImpl(
                object : MemberWorkoutDao {
                    override fun getWorkoutsForTrainee(traineeId: String) = flowOf(MemberWorkoutViewModel.previewWorkouts)
                    override fun getAllWorkouts() = flowOf(MemberWorkoutViewModel.previewWorkouts)
                    override suspend fun insertWorkout(workout: MemberWorkout) {}
                    override suspend fun updateWorkout(workout: MemberWorkout) {}
                    override suspend fun deleteWorkout(workout: MemberWorkout) {}
                    override suspend fun updateWorkoutCompletion(workoutId: Int, isCompleted: Boolean) {}
                    override fun getCompletedWorkoutsCount(traineeId: String) = flowOf(1)
                    override fun getTotalWorkoutsCount(traineeId: String) = flowOf(3)
                }
            )
        )
    )
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun MemberWorkoutScreenPreview(
    @PreviewParameter(PreviewMemberWorkoutProvider::class) viewModel: MemberWorkoutViewModel
) {
    GymManagementAppTheme {
        MemberWorkoutScreen(viewModel = viewModel)
    }
}

