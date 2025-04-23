package com.example.gymmanagement.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gymmanagement.data.dao.WorkoutDao
import com.example.gymmanagement.data.dao.EventDao
import com.example.gymmanagement.data.dao.MemberDao
import com.example.gymmanagement.data.model.Workout
import com.example.gymmanagement.data.model.Event
import com.example.gymmanagement.data.model.Member

@Database(
    entities = [Workout::class, Event::class, Member::class],
    version = 1,
    exportSchema = false
)
abstract class GymDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
    abstract fun eventDao(): EventDao
    abstract fun memberDao(): MemberDao

    companion object {
        @Volatile
        private var INSTANCE: GymDatabase? = null

        fun getDatabase(context: Context): GymDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GymDatabase::class.java,
                    "gym_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
} 