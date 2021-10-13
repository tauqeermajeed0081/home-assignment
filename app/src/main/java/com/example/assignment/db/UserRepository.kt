package com.example.assignment.db

import androidx.lifecycle.LiveData

class UserRepository(private val userDao: UserDAO) {

    val readAllData: LiveData<List<UserData>> = userDao.readAllData()

    suspend fun addUser(user: UserData) {
        userDao.addUser(user)
    }

}