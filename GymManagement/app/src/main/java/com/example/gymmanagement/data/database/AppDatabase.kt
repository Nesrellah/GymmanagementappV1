package com.example.gymmanagement.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gymmanagement.data.dao.*
import com.example.gymmanagement.data.model.*

@Database(
    entities = [
        UserEntity::class,
        UserProfile::class,
        Workout::class,
        EventEntity::class,
        MemberWorkout::class,
        MemberEvent::class,
        TraineeProgress::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun eventDao(): EventDao
    abstract fun memberWorkoutDao(): MemberWorkoutDao
    abstract fun memberEventDao(): MemberEventDao
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