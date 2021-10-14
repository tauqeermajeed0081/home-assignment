package com.example.assignment.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DateModel (
    var date: String = "",
    var itemList: ArrayList<ItemModel> = ArrayList()
): Parcelable