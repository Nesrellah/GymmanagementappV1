package com.example.gymmanagement

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.gymmanagement.navigation.AppNavigation
import com.example.gymmanagement.ui.theme.GymManagementAppTheme

class MainActivity : ComponentActivity() {
    private val TAG = "MainActivity"
    private lateinit var app: GymManagementApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate called")
        app = application as GymManagementApp

        // Use application context for SharedPreferences to ensure consistency
        val sharedPreferences = applicationContext.getSharedPreferences("user_session", Context.MODE_PRIVATE)
        
        // Check if we have a valid session
        val savedEmail = sharedPreferences.getString("user_email", null)
        val savedRole = sharedPreferences.getString("user_role", null)
        
        Log.d(TAG, "Current session state - email: $savedEmail, role: $savedRole")
        
        // Only set app_just_started to true if there's no existing session
        if (savedEmail == null) {
            Log.d(TAG, "No existing session found, setting app_just_started to true")
            sharedPreferences.edit().putBoolean("app_just_started", true).apply()
        } else {
            Log.d(TAG, "Existing session found, keeping app_just_started as is")
        }

        setContent {
            GymManagementAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(app)
                }
            }
        }
    }
}
