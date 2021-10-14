package com.example.assignment.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class UserData(
    var userExpense: Long = 0,
    var userIncome: Long = 0,
    var userBalance: Long = 0,
    var userTransactionList: ArrayList<DateModel>? = null
): Parcelable
