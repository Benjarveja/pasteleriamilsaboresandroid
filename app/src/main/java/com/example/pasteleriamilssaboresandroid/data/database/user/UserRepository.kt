package com.example.pasteleriamilssaboresandroid.data.database.user

import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    fun getUser(email: String): Flow<User> = userDao.getUser(email)

    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    suspend fun findByEmail(email: String): User? {
        return userDao.findByEmail(email)
    }
}

