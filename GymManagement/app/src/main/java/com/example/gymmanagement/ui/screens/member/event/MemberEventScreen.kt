package com.example.gymmanagement.ui.screens.member.event

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.gymmanagement.data.model.EventEntity
import com.example.gymmanagement.ui.theme.GymManagementAppTheme
import com.example.gymmanagement.viewmodel.MemberEventViewModel

private val DeepBlue = Color(0xFF0000CD)
private val LightBlue = Color(0xFFE6E9FD)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberEventScreen(
    onNavigateToWorkouts: () -> Unit,
    onNavigateToProgress: () -> Unit
) {
    val viewModel: MemberEventViewModel = viewModel()
    val events by viewModel.upcomingEvents.collectAsState(initial = emptyList())

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
                text = "Upcoming Events",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBlue
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = onNavigateToWorkouts,
                    modifier = Modifier
                        .background(LightBlue, CircleShape)
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Workouts",
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

        // Events List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(events) { event ->
                EventCard(event = event)
            }
        }
    }
}

@Composable
fun EventCard(event: EventEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box {
            // Event Image
            if (event.imageUri != null) {
                AsyncImage(
                    model = event.imageUri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

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
                // Title
                Text(
                    text = event.title,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                // Event Details
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EventDetailRow(
                        icon = Icons.Default.DateRange,
                        text = event.date
                    )
                    EventDetailRow(
                        icon = Icons.Default.Person,
                        text = event.time
                    )
                    EventDetailRow(
                        icon = Icons.Default.LocationOn,
                        text = event.location
                    )
                }
            }
        }
    }
}

@Composable
fun EventDetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Row(
        modifier = Modifier
            .background(
                color = Color.White.copy(alpha = 0.8f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = DeepBlue,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = text,
            color = Color.Black,
            fontSize = 14.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MemberEventScreenPreview() {
    GymManagementAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MemberEventScreen({}, {})
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventCardPreview() {
    GymManagementAppTheme {
        Surface(
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            EventCard(
                event = EventEntity(
                    id = 1,
                    title = "Fitness Workshop",
                    date = "2024-03-15",
                    time = "10:00 AM",
                    location = "Main Gym"
                )
            )
        }
    }
} 