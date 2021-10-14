package com.example.assignment.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemModel(
    var item: String? = null,
    var price: Int? = -1,
    var itemType: String? = ""
) : Parcelable
