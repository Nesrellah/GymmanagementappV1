package com.example.gymmanagement

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.gymmanagement.data.database.AppDatabase
import com.example.gymmanagement.data.model.UserEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var userDao: com.example.gymmanagement.data.dao.UserDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        userDao = db.userDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndReadUser() = runBlocking {
        val user = UserEntity(
            name = "Test User",
            email = "test@example.com",
            password = "password",
            age = 25,
            height = 175f,
            weight = 70f,
            role = "member",
            joinDate = "2024-03-15"
        )
        userDao.insertUser(user)
        
        val users = userDao.getAllUsers().first()
        assertEquals(1, users.size)
        assertEquals("Test User", users[0].name)
    }
} 