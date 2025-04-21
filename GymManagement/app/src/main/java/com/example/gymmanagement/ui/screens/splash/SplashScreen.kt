package com.example.gymmanagement.ui.screens.splash

import com.example.gymmanagement.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gymmanagement.ui.navigation.AppRoutes
import androidx.compose.ui.graphics.Color
import com.example.gymmanagement.ui.theme.Blue

@Composable
fun HeaderWithLogo() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(285.dp)
            .background(color = Blue) // Use your preferred blue color
//            .padding(24.dp)

    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            // Circular Logo
            Image(
                painter = painterResource(id = R.drawable.gym_logo),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape) // Makes the image circular
                    .border(
                        width = 3.dp, // Border thickness
                        color = Color.White, // Border color
                        shape = CircleShape // Matches the clip shape
                    ),
                contentScale = ContentScale.Crop // Ensures proper image scaling
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Text Column
                Text(
                    text = "FITNESS GYM",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            Spacer(modifier = Modifier.height(17.dp))
                Text(
                    text = "Your journey to a healthier life",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )

        }
    }
}


@Composable
fun ContactUsSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "📞 Contact Us",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Email: contact@fitzone.com")
        Text("Phone: +123 456 7890")
        Text("Location: 123 Fit Street, Wellness City")
    }
}

@Composable
fun SplashScreen(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
//            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            Image(
//                painter = painterResource(id = R.drawable.gym_logo),
//                contentDescription = "Gym Logo",
//                modifier = Modifier
//                    .size(120.dp)
//                    .clip(CircleShape), // This makes the IMAGE ITSELF circular
//                contentScale = ContentScale.Crop // Ensures the image fills the circle
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            Text(
//                text = "FITNESS GYM",
//                style = MaterialTheme.typography.headlineLarge,
//                fontWeight = FontWeight.Bold,
//                fontSize = 32.sp,
//                modifier = Modifier.padding(bottom = 8.dp)
//            )
//            Text(
//                text = "Your journey to a healthier life starts here",
//                style = MaterialTheme.typography.titleMedium,
//                textAlign = TextAlign.Center,
//                modifier = Modifier.padding(bottom = 32.dp)
//            )
            HeaderWithLogo()

            Spacer(modifier = Modifier.height(16.dp))

            ContactUsSection()

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = { navController.navigate(AppRoutes.LOGIN) },
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("Login")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { navController.navigate(AppRoutes.REGISTER) },
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("Register")
            }
        }
    }
}
