package com.example.assignment
import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.*


fun getCurrentTimeStamp(): String {
    val sdfDate = SimpleDateFormat("dd'th' MMMM, yyyy", Locale.getDefault())
    val now = Date()
    return sdfDate.format(now)
}
fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}
