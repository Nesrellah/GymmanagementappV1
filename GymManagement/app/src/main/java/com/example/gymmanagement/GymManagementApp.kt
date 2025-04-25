package com.example.gymmanagement

import android.app.Application
import com.example.gymmanagement.data.database.AppDatabase
import com.example.gymmanagement.data.repository.UserRepository
import com.example.gymmanagement.data.repository.UserRepositoryImpl

class GymManagementApp : Application() {
    // Database instance
    private val database by lazy { AppDatabase.getDatabase(this) }
    
    // Repository instances
    val userRepository: UserRepository by lazy {
        UserRepositoryImpl(
            userDao = database.userDao(),
            userProfileDao = database.userProfileDao(),
            context = applicationContext
        )
    }

    companion object {
        private lateinit var instance: GymManagementApp
        
        fun getInstance(): GymManagementApp {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
} 