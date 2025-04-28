import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberProfileScreen(
    userEmail: String,
    viewModel: MemberProfileViewModel = viewModel(),
    onLogout: () -> Unit = {}
) {
    var isEditing by remember { mutableStateOf(false) }
    val userProfile by viewModel.userProfile.collectAsState()

    LaunchedEffect(userEmail) {
        viewModel.getUserProfileByEmail(userEmail)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1A18C6)) // Deep blue
                .padding(vertical = 24.dp, horizontal = 16.dp)
        ) {
            Text(
                text = "Your profile",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Profile Icon
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .border(8.dp, Color(0xFF1A18C6), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = Color(0xFF1A18C6),
                    modifier = Modifier.size(100.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        userProfile?.let { profile ->
            if (!isEditing) {
                DisplayProfile(
                    profile = profile,
                    onEditClick = { isEditing = true }
                )
            } else {
                // ... your EditProfile composable ...
            }
        }
    }
}

@Composable
fun DisplayProfile(
    profile: UserProfile,
    onEditClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FB))
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Personal information",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    IconButton(onClick = onEditClick) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = Color.Black
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                ProfileField("Name:", profile.name)
                ProfileField("Email:", profile.email)
                profile.age?.let { ProfileField("Age:", "$it years") }
                profile.height?.let { ProfileField("Height:", "${it.toInt()}cm") }
                profile.weight?.let { ProfileField("Weight:", "${it.toInt()} kg") }
                profile.bmi?.let { ProfileField("BMI :", String.format("%.2f", it)) }
                profile.joinDate?.let { ProfileField("Join Date:", it) }
            }
        }
    }
}

@Composable
fun ProfileField(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 16.sp,
            modifier = Modifier.width(100.dp)
        )
        Text(
            text = value,
            color = Color.Gray,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}