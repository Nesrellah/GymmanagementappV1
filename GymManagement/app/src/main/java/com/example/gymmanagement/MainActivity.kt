package com.example.gymmanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.gymmanagement.navigation.AppNavigation
import com.example.gymmanagement.ui.theme.GymManagementAppTheme
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var app: GymManagementApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as GymManagementApp

        setContent {
            GymManagementAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //  Pass 'app' to AppNavigation
                    AppNavigation(app)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        // Launch a coroutine to safely call suspend function
        lifecycleScope.launch {
            val currentUser = app.userRepository.getCurrentUser()

            // You can act based on currentUser here (e.g., navigate or show login screen)
            if (currentUser == null) {
                // User is not logged in
                // You could trigger navigation to Login screen here if needed
            } else {
                // User is logged in
            }
        }
    }
}
