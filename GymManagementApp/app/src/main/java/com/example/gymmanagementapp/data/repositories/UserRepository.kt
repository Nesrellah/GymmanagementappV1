package com.example.gymmanagementapp.data.repositories

import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.gymmanagementapp.data.dao.UserDao
import com.example.gymmanagementapp.data.entities.User

class UserRepository(private val userDao: UserDao) {

    suspend fun registerUser(user: User): Boolean {
        return try {
            val hashedPassword = hashPassword(user.passwordHash)
            userDao.insertUser(user.copy(passwordHash = hashedPassword))
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun login(email: String, password: String): User? {
        val user = userDao.getUserByEmail(email)
        return if (user != null && verifyPassword(password, user.passwordHash)) user else null
    }

    private fun hashPassword(plainPassword: String): String {
        return BCrypt.withDefaults().hashToString(12, plainPassword.toCharArray())
    }

    private fun verifyPassword(plainPassword: String, hashedPassword: String): Boolean {
        return BCrypt.verifyer().verify(plainPassword.toCharArray(), hashedPassword).verified
    }
}