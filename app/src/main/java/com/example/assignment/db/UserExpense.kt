package com.example.assignment.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "user_transactions")
data class UserTransactions(
    @PrimaryKey
    val uEid: Int,
    @ColumnInfo(name = "transaction_title")
    var transactionTitle: String?,
    @ColumnInfo(name = "transaction_price")
    var transactionPrice: String?,
    @ColumnInfo(name = "transactionDate")
    var transactionDate: String?
)
