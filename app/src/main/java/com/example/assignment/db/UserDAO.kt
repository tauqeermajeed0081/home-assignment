package com.example.assignment.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDAO {
    @Query("SELECT * FROM user_table")
    fun readAllData(): LiveData<List<UserData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: UserData)
}