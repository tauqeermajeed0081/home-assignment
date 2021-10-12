package com.example.homeassignment
import java.text.SimpleDateFormat
import java.util.*


fun getCurrentTimeStamp(): String {
    val sdfDate = SimpleDateFormat("dd'th' MMMM, yyyy", Locale.getDefault())
    val now = Date()
    return sdfDate.format(now)
}
