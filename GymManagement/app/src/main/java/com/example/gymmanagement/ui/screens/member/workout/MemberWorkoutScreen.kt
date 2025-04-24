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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymmanagement.data.model.MemberWorkout
import com.example.gymmanagement.data.repository.MemberWorkoutRepository
import com.example.gymmanagement.data.repository.MemberWorkoutRepositoryImpl
import com.example.gymmanagement.data.dao.MemberWorkoutDao
import com.example.gymmanagement.ui.theme.GymManagementAppTheme
import com.example.gymmanagement.viewmodel.MemberWorkoutViewModel
import kotlinx.coroutines.flow.flowOf

@Composable
fun MemberWorkoutScreen(
    viewModel: MemberWorkoutViewModel
) {
    val workouts by viewModel.workouts.collectAsState()
    val progress by viewModel.progress.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "My Workouts",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Progress",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${(progress * 100).toInt()}% Complete",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        if (workouts.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No workouts assigned yet",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
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
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = workout.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Checkbox(
                    checked = workout.isCompleted,
                    onCheckedChange = { onToggleCompletion() }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Sets: ${workout.sets}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Reps/Seconds: ${workout.repsOrSecs}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Rest Time: ${workout.restTime} seconds",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun WorkoutInfo(label: String) {
    Surface(
        color = Color.White.copy(alpha = 0.9f),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = label,
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

