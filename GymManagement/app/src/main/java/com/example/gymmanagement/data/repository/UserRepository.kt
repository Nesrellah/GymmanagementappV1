package com.example.gymmanagement.data.repository

import android.content.Context
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
    private val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

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
        return savedEmail?.let { getUserProfileByEmail(it) }
    }

    override suspend fun saveCurrentUser(profile: UserProfile) {
        sharedPreferences.edit().apply {
            putString("user_email", profile.email)
            apply()
        }
    }

    override suspend fun clearCurrentUser() {
        sharedPreferences.edit().clear().apply()
    }
} 