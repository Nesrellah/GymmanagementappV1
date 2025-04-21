package com.example.gymmanagement.ui.screens.register

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gymmanagement.ui.navigation.AppRoutes
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymmanagement.viewmodel.AuthViewModel
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext



@Composable
fun RegisterScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    val authViewModel: AuthViewModel = viewModel()
    val context = LocalContext.current



    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Register", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") })
            OutlinedTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = { Text("Confirm Password") })
            OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Age") })
            OutlinedTextField(value = height, onValueChange = { height = it }, label = { Text("Height (cm)") })
            OutlinedTextField(value = weight, onValueChange = { weight = it }, label = { Text("Weight (kg)") })

            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = {
                if (password != confirmPassword) {
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                authViewModel.registerUser(
                    name = name,
                    email = email,
                    password = password,
                    age = age.toIntOrNull() ?: 0,
                    height = height.toFloatOrNull() ?: 0f,
                    weight = weight.toFloatOrNull() ?: 0f,
                    role = "member" // or "admin" if you want to test
                ) { success, message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    if (success) {
                        navController.navigate(AppRoutes.LOGIN)
                    }
                }
            }) {
                Text("Register")
            }


            TextButton(onClick = {
                navController.navigate(AppRoutes.LOGIN)
            }) {
                Text("Already have an account? Login")
            }
        }
    }
}
