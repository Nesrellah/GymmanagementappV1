package com.example.gymmanagement.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gymmanagement.R
import com.example.gymmanagement.navigation.AppRoutes
import com.example.gymmanagement.ui.theme.Blue
import com.example.gymmanagement.viewmodel.AuthViewModel

@Composable
fun WaveBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)  // Even smaller height
            .background(
                color = Blue,
                shape = RoundedCornerShape(
                    bottomStart = 35.dp,
                    bottomEnd = 35.dp
                )
            )
    ) {
        content()
    }
}

@Composable
fun HeaderWithLogo() {
    WaveBackground {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.gym_logo),
                contentDescription = "Gym Logo",
                modifier = Modifier
                    .size(65.dp)
                    .clip(CircleShape)
                    .border(
                        width = 2.dp,
                        color = Color.White,
                        shape = CircleShape
                    ),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "FITNESS GYM",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Your journey to a healthier life starts here",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }
    }
}

@Composable
fun ContactInfo(icon: ImageVector, text: String, showDivider: Boolean = true) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Blue,
                modifier = Modifier
                    .size(20.dp)
                    .padding(end = 8.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black.copy(alpha = 0.7f),
                fontSize = 14.sp
            )
        }
        if (showDivider) {
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun ContactUsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = "Contact Us",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp),
            fontSize = 16.sp
        )

        ContactInfo(Icons.Filled.Phone, "+251 90 102 0304")
        ContactInfo(Icons.Filled.Email, "info@fitnessgym.com")
        ContactInfo(Icons.Filled.LocationOn, "5 kilo, Addis Ababa, Ethiopia", false)
    }
}

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            HeaderWithLogo()

            Spacer(modifier = Modifier.height(8.dp))

            ContactUsSection()

            Spacer(modifier = Modifier.height(6.dp))

            // Gym Equipment Image
            Image(
                painter = painterResource(id = R.drawable.gym_logo),
                contentDescription = "Gym Equipment",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(120.dp),  // Reduced height
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Buttons at the bottom
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
            ) {
                // Login Button
                Button(
                    onClick = {
                        navController.navigate(AppRoutes.LOGIN) {
                            // Clear back stack up to splash screen
                            popUpTo(AppRoutes.SPLASH) { inclusive = false }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(4.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp
                    )
                ) {
                    Text(
                        "Login",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Register Button - White background with blue border
                OutlinedButton(
                    onClick = {
                        navController.navigate(AppRoutes.REGISTER) {
                            // Clear back stack up to splash screen
                            popUpTo(AppRoutes.SPLASH) { inclusive = false }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(Color.White, RoundedCornerShape(4.dp)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Blue,
                        containerColor = Color.White
                    ),
                    border = BorderStroke(1.dp, Blue),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        "Register",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}