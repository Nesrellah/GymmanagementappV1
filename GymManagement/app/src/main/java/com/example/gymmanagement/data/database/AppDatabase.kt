package com.example.gymmanagement.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gymmanagement.data.dao.UserDao
import com.example.gymmanagement.data.dao.WorkoutDao
import com.example.gymmanagement.data.dao.EventDao
import com.example.gymmanagement.data.dao.MemberWorkoutDao
import com.example.gymmanagement.data.model.UserEntity
import com.example.gymmanagement.data.model.Workout
import com.example.gymmanagement.data.model.EventEntity
import com.example.gymmanagement.data.model.MemberWorkout

@Database(
    entities = [
        UserEntity::class,
        Workout::class,
        EventEntity::class,
        MemberWorkout::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun eventDao(): EventDao
    abstract fun memberWorkoutDao(): MemberWorkoutDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gym_management_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
} 