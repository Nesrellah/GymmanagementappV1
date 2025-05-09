package com.example.gymmanagement.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gymmanagement.data.dao.*
import com.example.gymmanagement.data.model.UserEntity
import com.example.gymmanagement.data.model.UserProfile
import com.example.gymmanagement.data.model.MemberWorkout
import com.example.gymmanagement.data.model.EventEntity
import com.example.gymmanagement.data.model.Workout
import com.example.gymmanagement.data.model.TraineeProgress

@Database(
    entities = [
        UserEntity::class,
        UserProfile::class,
        MemberWorkout::class,
        EventEntity::class,
        Workout::class,
        TraineeProgress::class
    ],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun memberWorkoutDao(): MemberWorkoutDao
    abstract fun eventDao(): EventDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun traineeProgressDao(): TraineeProgressDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gym_management_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
} 