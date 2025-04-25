package com.example.gymmanagement.data.repository

import android.content.Context
import android.util.Log
import com.example.gymmanagement.data.dao.UserDao
import com.example.gymmanagement.data.dao.UserProfileDao
import com.example.gymmanagement.data.model.UserEntity
import com.example.gymmanagement.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    // UserEntity operations
    fun getAllUsers(): Flow<List<UserEntity>>
    suspend fun getUserByEmail(email: String): UserEntity?
    suspend fun login(email: String, password: String): UserEntity?
    suspend fun insertUser(user: UserEntity)
    suspend fun updateUser(user: UserEntity)
    suspend fun deleteUser(user: UserEntity)

    // UserProfile operations
    fun getAllUserProfiles(): Flow<List<UserProfile>>
    suspend fun getUserProfileById(id: Int): UserProfile?
    suspend fun getUserProfileByEmail(email: String): UserProfile?
    suspend fun insertUserProfile(profile: UserProfile)
    suspend fun updateUserProfile(profile: UserProfile)
    suspend fun deleteUserProfile(profile: UserProfile)

    // Session management
    suspend fun getCurrentUser(): UserProfile?
    suspend fun saveCurrentUser(profile: UserProfile)
    suspend fun clearCurrentUser()
}

class UserRepositoryImpl(
    private val userDao: UserDao,
    private val userProfileDao: UserProfileDao,
    private val context: Context
) : UserRepository {
    private val sharedPreferences = context.applicationContext.getSharedPreferences("user_session", Context.MODE_PRIVATE)
    private val TAG = "UserRepository"

    // UserEntity operations
    override fun getAllUsers(): Flow<List<UserEntity>> = userDao.getAllUsers()
    override suspend fun getUserByEmail(email: String): UserEntity? = userDao.getUserByEmail(email)
    override suspend fun login(email: String, password: String): UserEntity? = userDao.login(email, password)
    override suspend fun insertUser(user: UserEntity) = userDao.insertUser(user)
    override suspend fun updateUser(user: UserEntity) = userDao.updateUser(user)
    override suspend fun deleteUser(user: UserEntity) = userDao.deleteUser(user)

    // UserProfile operations
    override fun getAllUserProfiles(): Flow<List<UserProfile>> = userProfileDao.getAllUserProfiles()
    override suspend fun getUserProfileById(id: Int): UserProfile? = userProfileDao.getUserProfileById(id)
    override suspend fun getUserProfileByEmail(email: String): UserProfile? = userProfileDao.getUserProfileByEmail(email)
    override suspend fun insertUserProfile(profile: UserProfile) = userProfileDao.insertUserProfile(profile)
    override suspend fun updateUserProfile(profile: UserProfile) = userProfileDao.updateUserProfile(profile)
    override suspend fun deleteUserProfile(profile: UserProfile) = userProfileDao.deleteUserProfile(profile)

    // Session management
    override suspend fun getCurrentUser(): UserProfile? {
        val savedEmail = sharedPreferences.getString("user_email", null)
        val savedRole = sharedPreferences.getString("user_role", null)
        val isAppJustStarted = sharedPreferences.getBoolean("app_just_started", true)
        
        Log.d(TAG, "getCurrentUser - savedEmail: $savedEmail, savedRole: $savedRole, isAppJustStarted: $isAppJustStarted")
        
        // If there's no saved email, return null
        if (savedEmail == null) {
            Log.d(TAG, "No saved email found, returning null")
            return null
        }
        
        // For admin users, we need to check if the app was just started
        if (savedRole?.lowercase() == "admin") {
            if (isAppJustStarted) {
                Log.d(TAG, "Admin session detected on app start, keeping session active")
                // Instead of clearing the session, just set app_just_started to false
                sharedPreferences.edit().putBoolean("app_just_started", false).apply()
            }
        }
        
        // Try to get the user profile
        val profile = getUserProfileByEmail(savedEmail)
        if (profile == null) {
            Log.d(TAG, "No profile found for email: $savedEmail, clearing session")
            clearCurrentUser()
            return null
        }
        
        return profile
    }

    override suspend fun saveCurrentUser(profile: UserProfile) {
        Log.d(TAG, "Saving current user: ${profile.email}, role: ${profile.role}")
        sharedPreferences.edit().apply {
            putString("user_email", profile.email)
            putString("user_role", profile.role)
            putBoolean("app_just_started", false)
            apply()
        }
        Log.d(TAG, "User saved, app_just_started set to false")
    }

    override suspend fun clearCurrentUser() {
        Log.d(TAG, "Clearing current user session")
        sharedPreferences.edit().apply {
            clear()
            // Don't set app_just_started to true here, let MainActivity handle it
            apply()
        }
        Log.d(TAG, "Session cleared")
    }
}
