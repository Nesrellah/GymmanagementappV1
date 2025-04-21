package com.example.gymmanagement.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gymmanagement.ui.navigation.AppRoutes
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymmanagement.viewmodel.AuthViewModel
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext



@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authViewModel: AuthViewModel = viewModel()
    val context = LocalContext.current



    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Login", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") }
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                authViewModel.login(email, password) { user ->
                    if (user != null) {
                        Toast.makeText(context, "Welcome ${user.name}", Toast.LENGTH_SHORT).show()

                        if (user.role == "admin") {
                            navController.navigate(AppRoutes.ADMIN_WORKOUT)
                        } else {
                            navController.navigate(AppRoutes.MEMBER_WORKOUT)
                        }
                    } else {
                        Toast.makeText(context, "Invalid email or password", Toast.LENGTH_SHORT).show()
                    }
                }
            }) {
                Text("Login")
            }


            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = {
                navController.navigate(AppRoutes.REGISTER)
            }) {
                Text("Don't have an account? Register")
            }
        }
    }
}
