@file:Suppress("DEPRECATION")

package com.enigmatix.deprocrastinator

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import java.util.*
import kotlin.time.measureTime

data class DateTime(
    var year: Int,
    var month: Int,
    var day: Int,
    var hour: Int,
    var minute: Int
)

fun prettyTimeString(date: Date?, noTime: Boolean = false): String {
    if (date == null)
        return ""
    var str1 = date.hours.toString()
    if (str1.length == 1)
        str1 = "0$str1"
    var str2 = date.minutes.toString()
    if (str2.length == 1)
        str2 = "0$str2"
    if (!noTime)
        return "${date.date}-${date.month+1}-${date.year+1900} $str1:$str2"
    return "${date.date}-${date.month+1}-${date.year+1900}"
}

fun prettyTimeString(datetime: DateTime): String {
    var str1 = datetime.hour.toString()
    if (str1.length == 1)
        str1 = "0$str1"
    var str2 = datetime.minute.toString()
    if (str2.length == 1)
        str2 = "0$str2"
    return "${datetime.day}-${datetime.month}-${datetime.year} $str1:$str2"
}

fun hideKeyboard(activity: Activity) {
    val inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    val currentFocusedView = activity.currentFocus
    currentFocusedView?.let {
        inputMethodManager.hideSoftInputFromWindow(
                currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}