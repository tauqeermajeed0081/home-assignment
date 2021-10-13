package com.example.assignment.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserData(
    @PrimaryKey
    val uid: Int,
    @ColumnInfo(name = "expenses")
    var expenses: Int?,
    @ColumnInfo(name = "income")
    var income: Int?,
    @ColumnInfo(name = "balance")
    var balance: Int?
)
