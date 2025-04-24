package com.example.gymmanagement.ui.screens.admin.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymmanagement.data.model.TraineeProgress
import com.example.gymmanagement.data.repository.TraineeProgressRepository
import com.example.gymmanagement.ui.theme.GymManagementAppTheme
import com.example.gymmanagement.viewmodel.AdminProgressViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AdminProgressScreen(
    viewModel: AdminProgressViewModel = viewModel()
) {
    val progress by viewModel.allProgress.collectAsState(initial = emptyList())
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Trainee Progress",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(progress) { traineeProgress ->
                TraineeProgressItem(
                    progress = traineeProgress,
                    onUpdate = { viewModel.updateProgress(it) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TraineeProgressItem(
    progress: TraineeProgress,
    onUpdate: (TraineeProgress) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Trainee ID: ${progress.traineeId}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Completed Workouts: ${progress.completedWorkouts}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Total Workouts: ${progress.totalWorkouts}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Progress: ${progress.progressPercentage}%",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminProgressScreenPreview() {
    val previewRepository = object : TraineeProgressRepository {
        override fun getAllProgress(): Flow<List<TraineeProgress>> = flowOf(
            listOf(
                TraineeProgress(
                    id = 1,
                    traineeId = "1",
                    completedWorkouts = 3,
                    totalWorkouts = 5
                ),
                TraineeProgress(
                    id = 2,
                    traineeId = "2",
                    completedWorkouts = 2,
                    totalWorkouts = 5
                ),
                TraineeProgress(
                    id = 3,
                    traineeId = "3",
                    completedWorkouts = 4,
                    totalWorkouts = 5
                )
            )
        )
        override suspend fun getProgressById(id: Int): TraineeProgress? = null
        override fun getProgressByTraineeId(traineeId: String): Flow<List<TraineeProgress>> = flowOf(emptyList())
        override suspend fun insertProgress(progress: TraineeProgress) {}
        override suspend fun updateProgress(progress: TraineeProgress) {}
        override suspend fun deleteProgress(progress: TraineeProgress) {}
    }

    GymManagementAppTheme {
        AdminProgressScreen(
            viewModel = AdminProgressViewModel(previewRepository)
        )
    }
} 