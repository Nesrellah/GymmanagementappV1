package com.example.gymmanagement

import android.app.Application
import com.example.gymmanagement.data.database.AppDatabase

class GymManagementApp : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()
    }
} 