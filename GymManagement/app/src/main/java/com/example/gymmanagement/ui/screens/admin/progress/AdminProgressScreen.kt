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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymmanagement.data.model.TraineeProgress
import com.example.gymmanagement.ui.theme.GymManagementAppTheme
import com.example.gymmanagement.viewmodel.AdminProgressViewModel

private val DeepBlue = Color(0xFF0000CD)
private val LightBlue = Color(0xFFE6E9FD)

@Composable
fun AdminProgressScreen(
    viewModel: AdminProgressViewModel = viewModel()
) {
    val trainees by viewModel.trainees.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "Gym Progress",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = DeepBlue,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Daily Progress",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(trainees) { trainee ->
                ProgressCard(trainee)
            }
        }
    }
}

@Composable
fun ProgressCard(trainee: TraineeProgress) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = LightBlue
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = trainee.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Text(
                    text = "${trainee.completedWorkouts}/${trainee.totalWorkouts} workouts completed",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = "${trainee.progressPercentage}%",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBlue
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun ProgressCardPreview() {
    GymManagementAppTheme {
        ProgressCard(
            trainee = TraineeProgress(
                id = 1,
                name = "Abel Melaku",
                email = "abel@example.com",
                completedWorkouts = 3,
                totalWorkouts = 5
            )
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun AdminProgressScreenPreview() {
    GymManagementAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AdminProgressScreen(
                viewModel = AdminProgressViewModel()
            )
        }
    }
} 