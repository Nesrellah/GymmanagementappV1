package com.example.gymmanagement.ui.screens.register

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gymmanagement.GymManagementApp
import com.example.gymmanagement.navigation.AppRoutes
import com.example.gymmanagement.ui.theme.Blue
import com.example.gymmanagement.viewmodel.AuthViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    val registerError by viewModel.registerError.collectAsState()
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Text(
                text = "Register",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Registration Form
        OutlinedTextField(
            value = name,
            onValueChange = { 
                name = it
                showError = false
            },
            label = { Text("Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            isError = showError && viewModel.validateName(name) != null,
            supportingText = {
                if (showError && viewModel.validateName(name) != null) {
                    Text(
                        text = viewModel.validateName(name) ?: "",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        OutlinedTextField(
            value = email,
            onValueChange = { 
                email = it
                showError = false
            },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            isError = showError && viewModel.validateEmail(email) != null,
            supportingText = {
                if (showError && viewModel.validateEmail(email) != null) {
                    Text(
                        text = viewModel.validateEmail(email) ?: "",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        OutlinedTextField(
            value = password,
            onValueChange = { 
                password = it
                showError = false
            },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            isError = showError && viewModel.validatePassword(password) != null,
            supportingText = {
                if (showError && viewModel.validatePassword(password) != null) {
                    Text(
                        text = viewModel.validatePassword(password) ?: "",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        OutlinedTextField(
            value = age,
            onValueChange = { 
                age = it
                showError = false
            },
            label = { Text("Age") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            isError = showError && viewModel.validateAge(age) != null,
            supportingText = {
                if (showError && viewModel.validateAge(age) != null) {
                    Text(
                        text = viewModel.validateAge(age) ?: "",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        OutlinedTextField(
            value = height,
            onValueChange = { 
                height = it
                showError = false
            },
            label = { Text("Height (cm)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            isError = showError && viewModel.validateHeight(height) != null,
            supportingText = {
                if (showError && viewModel.validateHeight(height) != null) {
                    Text(
                        text = viewModel.validateHeight(height) ?: "",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        OutlinedTextField(
            value = weight,
            onValueChange = { 
                weight = it
                showError = false
            },
            label = { Text("Weight (kg)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            isError = showError && viewModel.validateWeight(weight) != null,
            supportingText = {
                if (showError && viewModel.validateWeight(weight) != null) {
                    Text(
                        text = viewModel.validateWeight(weight) ?: "",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        if (registerError != null) {
            Text(
                text = registerError ?: "",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Button(
            onClick = {
                showError = true
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
        ) {
            Text("Register")
        }

        TextButton(
            onClick = { navController.navigate(AppRoutes.LOGIN) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Already have an account? Login")
        }
    }
}