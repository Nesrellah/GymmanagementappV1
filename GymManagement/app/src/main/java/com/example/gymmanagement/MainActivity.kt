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
import androidx.lifecycle.lifecycleScope
import com.example.gymmanagement.data.database.AppDatabase
import com.example.gymmanagement.data.model.UserEntity
import kotlinx.coroutines.launch

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

//        lifecycleScope.launch {
//            val db = AppDatabase.getDatabase(applicationContext)
//            val adminUser = UserEntity(
//                id = 0,
//                name = "Admin",
//                email = "admin@gmail.com",
//                password = "admin123",
//                age = 30,
//                height = 180f,
//                weight = 75f,
//                role = "admin",
//                joinDate = "2024-06-01"
//            )
//            db.userDao().insertUser(adminUser)
//        }
    }
}
