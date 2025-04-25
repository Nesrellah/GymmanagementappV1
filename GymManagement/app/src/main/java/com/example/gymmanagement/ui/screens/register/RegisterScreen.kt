package com.example.gymmanagement.ui.screens.register

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gymmanagement.navigation.AppRoutes
import com.example.gymmanagement.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    val registerError by viewModel.registerError.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        IconButton(
            onClick = { navController.navigateUp() },
            modifier = Modifier.padding(start = 24.dp, top = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
        ) {
            // Add spacing instead of offset
            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0000CD)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }

            Text(
                text = "Create Account",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(top = 16.dp)
            )

            Text(
                text = "Join our fitness community",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
            )

            // --- Name Field ---
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                placeholder = { Text("Enter your full name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = if (showError && viewModel.validateName(name) != null) 4.dp else 16.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF0000CD),
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                ),
                isError = showError && viewModel.validateName(name) != null
            )
            if (showError && viewModel.validateName(name) != null) {
                Text(
                    text = viewModel.validateName(name) ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // --- Email Field ---
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                placeholder = { Text("Enter your Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = if (showError && viewModel.validateEmail(email) != null) 4.dp else 16.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF0000CD),
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                ),
                isError = showError && viewModel.validateEmail(email) != null
            )
            if (showError && viewModel.validateEmail(email) != null) {
                Text(
                    text = viewModel.validateEmail(email) ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // --- Password Field ---
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                placeholder = { Text("Create Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = if (showError && viewModel.validatePassword(password) != null) 4.dp else 16.dp),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF0000CD),
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                ),
                isError = showError && viewModel.validatePassword(password) != null
            )
            if (showError && viewModel.validatePassword(password) != null) {
                Text(
                    text = viewModel.validatePassword(password) ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // --- Confirm Password ---
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                placeholder = { Text("Confirm your password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = if (showError && password != confirmPassword) 4.dp else 16.dp),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF0000CD),
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                ),
                isError = showError && password != confirmPassword
            )
            if (showError && password != confirmPassword) {
                Text(
                    text = "Passwords do not match",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // --- Age Field ---
            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Age") },
                placeholder = { Text("Enter your Age") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = if (showError && viewModel.validateAge(age) != null) 4.dp else 16.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF0000CD),
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                ),
                isError = showError && viewModel.validateAge(age) != null
            )
            if (showError && viewModel.validateAge(age) != null) {
                Text(
                    text = viewModel.validateAge(age) ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // --- Height Field ---
            OutlinedTextField(
                value = height,
                onValueChange = { height = it },
                label = { Text("Height (cm)") },
                placeholder = { Text("Enter your height in cm") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = if (showError && viewModel.validateHeight(height) != null) 4.dp else 16.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF0000CD),
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                ),
                isError = showError && viewModel.validateHeight(height) != null
            )
            if (showError && viewModel.validateHeight(height) != null) {
                Text(
                    text = viewModel.validateHeight(height) ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // --- Weight Field ---
            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Weight (kg)") },
                placeholder = { Text("Enter your weight in kg") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = if (showError && viewModel.validateWeight(weight) != null) 4.dp else 16.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF0000CD),
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                ),
                isError = showError && viewModel.validateWeight(weight) != null
            )
            if (showError && viewModel.validateWeight(weight) != null) {
                Text(
                    text = viewModel.validateWeight(weight) ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // --- Register Button ---
            Button(
                onClick = {
                    showError = true
                    if (password != confirmPassword) {
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (viewModel.validateName(name) == null &&
                        viewModel.validateEmail(email) == null &&
                        viewModel.validatePassword(password) == null &&
                        viewModel.validateAge(age) == null &&
                        viewModel.validateHeight(height) == null &&
                        viewModel.validateWeight(weight) == null
                    ) {
                        viewModel.registerUser(
                            name = name,
                            email = email,
                            password = password,
                            age = age,
                            height = height,
                            weight = weight
                        ) { success, message ->
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            if (success) {
                                navController.navigate(AppRoutes.LOGIN) {
                                    popUpTo(AppRoutes.REGISTER) { inclusive = true }
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0000CD)),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text("Register", fontSize = 16.sp)
            }

            // --- Login Link ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Already have an account? ",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Text(
                    text = "Login",
                    color = Color(0xFF0000CD),
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable {
                        navController.navigate(AppRoutes.LOGIN) {
                            popUpTo(AppRoutes.REGISTER) { inclusive = true }
                        }
                    }
                )
            }
        }
    }

    // Toast error
    registerError?.let { error ->
        if (error.isNotEmpty()) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }
}
