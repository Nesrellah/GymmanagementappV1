package com.example.gymmanagementapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gymmanagementapp.data.dao.UserDao
import com.example.gymmanagementapp.data.dao.WorkoutDao
import com.example.gymmanagementapp.data.entities.User
import com.example.gymmanagementapp.data.entities.Workout
import com.example.gymmanagementapp.data.entities.Event

// AppDatabase.kt
@Database(
    entities = [User::class, Workout::class, Event::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun workoutDao(): WorkoutDao

    companion object {
        // Singleton pattern to prevent multiple DB instances
        fun getDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "gym_database.db"
            ).build()
        }
    }
}