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
